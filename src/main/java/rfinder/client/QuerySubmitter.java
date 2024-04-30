package rfinder.client;

import rfinder.query.QueryInfo;
import rfinder.query.result.QuerySolution;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface QuerySubmitter {
    CompletableFuture<List<QuerySolution>> submit(QueryInfo queryInfo);
}
