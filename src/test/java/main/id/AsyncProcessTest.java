package main.id;

import id.AsyncProcess;
import id.IAsyncService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AsyncProcessTest {

    @Test
    public void testFactAsyncData() throws ExecutionException, InterruptedException {

        IAsyncService mock = mock(IAsyncService.class);

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "dadada");
        when(mock.factData("321")).thenReturn(completableFuture);

        AsyncProcess asyncProcess = new AsyncProcess(mock);
        CompletableFuture<String> actualFuture = asyncProcess.asyncProcess("321");
        assertEquals("Data:dadada", actualFuture.get());
        verify(mock).factData("321");
    }
}
