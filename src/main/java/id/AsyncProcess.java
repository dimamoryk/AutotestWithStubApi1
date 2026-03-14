package id;

import java.util.concurrent.CompletableFuture;

public class AsyncProcess {

    private IAsyncService asyncService;

    public AsyncProcess (IAsyncService asyncService){
        this.asyncService = asyncService;
    }

    public CompletableFuture<String> asyncProcess(String id){
        return asyncService.factData(id).thenApply(data -> "Data:" + data);
    }
}
