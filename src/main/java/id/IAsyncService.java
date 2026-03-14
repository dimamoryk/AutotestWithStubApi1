package id;

import java.util.concurrent.CompletableFuture;

public interface IAsyncService {

    CompletableFuture<String> factData(String id);
}
