
package view.map;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SingleSelectionModel;
import org.mapsforge.core.graphics.*;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.core.util.Parameters;
import org.mapsforge.core.util.Utils;
import org.mapsforge.map.awt.graphics.AwtBitmap;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;
import org.mapsforge.map.awt.util.AwtUtil;
import org.mapsforge.map.awt.util.JavaPreferences;
import org.mapsforge.map.awt.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.datastore.MultiMapDataStore;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.debug.TileCoordinatesLayer;
import org.mapsforge.map.layer.debug.TileGridLayer;
import org.mapsforge.map.layer.download.TileDownloadLayer;
import org.mapsforge.map.layer.download.tilesource.TileSource;
import org.mapsforge.map.layer.hills.DemFolderFS;
import org.mapsforge.map.layer.hills.DiffuseLightShadingAlgorithm;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.layer.hills.MemoryCachingHgtReaderTileSource;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.IMapViewPosition;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.model.common.PreferencesFacade;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import view.SidePanelController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.prefs.Preferences;


public final class MapController extends JFrame {
    private static final GraphicFactory GRAPHIC_FACTORY = AwtGraphicFactory.INSTANCE;
    private static final boolean SHOW_DEBUG_LAYERS = false;

    private static final String MESSAGE = "Quit?";
    private static final String TITLE = "Confirm close";
    private final MapView mapView = createMapView();

    private Marker sourceMarker = new Marker(new LatLong(0,0),sourceBitmap,0,0);
    private Marker destinationMarker = new Marker(new LatLong(0,0),destMarkerBitmap,0,0);
    private static Bitmap destMarkerBitmap;
    private static Bitmap sourceBitmap;

    private SidePanelController sidePanelController;

    private static final int MARKER_SIZE = 20;


    static {
        Bitmap bitmap = null;

        try {
            destMarkerBitmap = new AwtBitmap(Objects.requireNonNull(MapController.class.getResourceAsStream("marker.png")));
            destMarkerBitmap.scaleTo(MARKER_SIZE, MARKER_SIZE);

            sourceBitmap = new AwtBitmap(Objects.requireNonNull(MapController.class.getResourceAsStream("marker_blue.png")));
            sourceBitmap.scaleTo(MARKER_SIZE, MARKER_SIZE);

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static class PointSelectionModel extends SingleSelectionModel<PointSelectionModel.SelectionPoint> {

        public enum SelectionPoint {SOURCE, DESTINATION}

        @Override
        protected SelectionPoint getModelItem(int index) {
            return SelectionPoint.values()[index];
        }

        @Override
        protected int getItemCount() {
            return 2;
        }

    }

    private final PointSelectionModel pointSelectionModel = new PointSelectionModel();


    public MapController() {
        Parameters.SQUARE_FRAME_BUFFER = false;

        HillsRenderConfig hillsCfg = null;
        File demFolder = getDemFolder();
        if (demFolder != null) {
            MemoryCachingHgtReaderTileSource tileSource = new MemoryCachingHgtReaderTileSource(new DemFolderFS(demFolder), new DiffuseLightShadingAlgorithm(), AwtGraphicFactory.INSTANCE);
            tileSource.setEnableInterpolationOverlap(true);
            hillsCfg = new HillsRenderConfig(tileSource);
            hillsCfg.indexOnThread();
        }

        List<File> mapFiles = getMapFiles();
        final BoundingBox boundingBox = addLayers(mapView, mapFiles, hillsCfg);

        initWindow(boundingBox);
        pointSelectionModel.select(0);
    }

    private void initWindow(BoundingBox boundingBox) {
        FXMLLoader fxmlLoader = new FXMLLoader(SidePanelController.class.getResource("side_panel.fxml"));

        final JFrame frame = new JFrame();
        sidePanelController = new SidePanelController();

        // set loader controller, load and initialize the side pane
        Platform.startup(()->{
            fxmlLoader.setController(sidePanelController);
            Scene sidePanelJFXScene;

            JFXPanel jfxPanel = new JFXPanel();
            frame.add(jfxPanel, BorderLayout.EAST);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            try{
                sidePanelJFXScene = new Scene(fxmlLoader.load());
                jfxPanel.setScene(sidePanelJFXScene);
            }
            catch (Exception exception){
                System.err.println("Could not open map window\n" + exception.getMessage());
            }

            sidePanelController.getDestinationField().focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldVal, Boolean newVal) {
                    pointSelectionModel.select(newVal ? PointSelectionModel.SelectionPoint.DESTINATION : PointSelectionModel.SelectionPoint.SOURCE);
                }
            });

            sidePanelController.getSourceField().focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldVal, Boolean newVal) {
                    pointSelectionModel.select(newVal ? PointSelectionModel.SelectionPoint.SOURCE : PointSelectionModel.SelectionPoint.DESTINATION);
                }
            });
        });

        try {
            SwingUtilities.invokeAndWait(()->{
                frame.setTitle("RFINDER");
                frame.add(mapView);
                frame.pack();
                frame.setSize(new Dimension(1000, 800));
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            });
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }


        final PreferencesFacade preferencesFacade = new JavaPreferences(Preferences.userNodeForPackage(MapController.class));

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(frame, MESSAGE, TITLE, JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    mapView.getModel().save(preferencesFacade);
                    mapView.destroyAll();
                    AwtGraphicFactory.clearResourceMemoryCache();
                    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                }
            }

            @Override
            public void windowOpened(WindowEvent e) {
                final Model model = mapView.getModel();
                model.init(preferencesFacade);
                if (model.mapViewPosition.getZoomLevel() == 0 || !boundingBox.contains(model.mapViewPosition.getCenter())) {
                    byte zoomLevel = LatLongUtils.zoomForBounds(model.mapViewDimension.getDimension(), boundingBox, model.displayModel.getTileSize());
                    model.mapViewPosition.setMapPosition(new MapPosition(boundingBox.getCenterPoint(), zoomLevel));
                }
            }
        });

        try {
            SwingUtilities.invokeAndWait(()->{
                frame.setVisible(true);
            });
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private BoundingBox addLayers(MapView mapView, List<File> mapFiles, HillsRenderConfig hillsRenderConfig)  {
        Layers layers = mapView.getLayerManager().getLayers();

        int tileSize = 300;

        // Tile cache
        TileCache tileCache = AwtUtil.createTileCache(
                tileSize,
                mapView.getModel().frameBufferModel.getOverdrawFactor(),
                65536,
                new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString()));

        final BoundingBox boundingBox;

        // Vector
        mapView.getModel().displayModel.setFixedTileSize(tileSize);
        MultiMapDataStore mapDataStore = new MultiMapDataStore(MultiMapDataStore.DataPolicy.RETURN_ALL);
        for (File file : mapFiles) {
            mapDataStore.addMapDataStore(new MapFile(file), false, false);
        }

        TileRendererLayer tileRendererLayer = createTileRendererLayer(tileCache, mapDataStore, mapView.getModel().mapViewPosition, hillsRenderConfig);

        layers.add(tileRendererLayer);

        Paint paint = GRAPHIC_FACTORY.createPaint();
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLUE);
        paint.setStyle(Style.STROKE);
        Polyline polyline = new Polyline(paint, GRAPHIC_FACTORY);
        polyline.setVisible(true);
        polyline.addPoints(List.of(new LatLong(38.1217046, 13.3535291), new LatLong(38.1104102, 13.3625928), new LatLong(38.1159136, 13.3696240), new LatLong(38.1477512, 13.3650464)));
        layers.add(polyline);

        // Request a redraw to update the map view
        boundingBox = mapDataStore.boundingBox();

        // Debug
        if (SHOW_DEBUG_LAYERS) {
            layers.add(new TileGridLayer(GRAPHIC_FACTORY, mapView.getModel().displayModel));
            layers.add(new TileCoordinatesLayer(GRAPHIC_FACTORY, mapView.getModel().displayModel));
        }

        return boundingBox;
    }

    private MapView createMapView() {
        MapView mapView = new MapView();
        mapView.getMapScaleBar().setVisible(true);
        if (SHOW_DEBUG_LAYERS) {
            mapView.getFpsCounter().setVisible(true);
        }

        return mapView;
    }

    @SuppressWarnings("unused")
    private TileDownloadLayer createTileDownloadLayer(TileCache tileCache, IMapViewPosition mapViewPosition, TileSource tileSource) {
        return new TileDownloadLayer(tileCache, mapViewPosition, tileSource, GRAPHIC_FACTORY) {
            @Override
            public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
                System.out.println("Tap on download layer: " + tapLatLong);
                return true;
            }
        };
    }

    private TileRendererLayer createTileRendererLayer(TileCache tileCache, MapDataStore mapDataStore, IMapViewPosition mapViewPosition, HillsRenderConfig hillsRenderConfig) {
        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore, mapViewPosition, false, true, true, GRAPHIC_FACTORY, hillsRenderConfig) {
            @Override
            public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
                System.out.println("Tap on: " + tapLatLong);
                PointSelectionModel.SelectionPoint point = pointSelectionModel.getSelectedItem();

                switch (point){
                    case SOURCE -> handleNewSource(tapLatLong);
                    case DESTINATION -> handleNewDestination(tapLatLong);
                }

                return true;
            }
        };
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);
        return tileRendererLayer;
    }

    private void handleNewSource(LatLong tapLatLong) {
        mapView.getLayerManager().getLayers().remove(sourceMarker);
        sourceMarker = new Marker(tapLatLong, sourceBitmap, 0, 0);
        mapView.getLayerManager().getLayers().add(sourceMarker);
        Platform.runLater(()->{
            sidePanelController.setSourcePoint(tapLatLong);
        });
    }

    private void handleNewDestination(LatLong tapLatLong) {
        mapView.getLayerManager().getLayers().remove(destinationMarker);
        destinationMarker = new Marker(tapLatLong, destMarkerBitmap, 0, 0);
        mapView.getLayerManager().getLayers().add(destinationMarker);
        Platform.runLater(()->{
            sidePanelController.setDestinationPoint(tapLatLong);
        });
    }


    private File getDemFolder() {

        File demFolder = new File("dem");
        if (demFolder.exists() && demFolder.isDirectory() && demFolder.canRead()) {
            return demFolder;
        }
        return null;
    }

    private static List<File> getMapFiles() {
        return List.of(new File("palermo.map"));
    }


}