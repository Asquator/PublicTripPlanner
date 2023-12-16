package org.example;

import rfinder.dao.DBManager;
import rfinder.dao.PostgisRouteDAO;
import rfinder.dao.RouteDAO;
import rfinder.structures.general.Location;
import rfinder.structures.nodes.StopNode;

import java.sql.Connection;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws Exception {
        RouteDAO dao = new PostgisRouteDAO();
        System.out.println(dao.getClosestVertex(Location.fromValues(13.330471,38.182302)));
    }
}
