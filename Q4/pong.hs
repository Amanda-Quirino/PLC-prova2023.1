import Control.Concurrent
import Control.Concurrent.STM

-- Modulo que recebe as mensagens
recebe :: TVar String -> MVar Int -> IO()
recebe tv fim = do
    x <- atomically (readTVar tv)
    printa tv fim x
        where
            printa tv fim "-5" = do
                putStrLn "Pong: -5"
                i <- takeMVar fim
                putMVar fim (i-1)
            printa tv fim nu = do
                putStrLn $ "Pong: " ++ nu
                i <- atomically (readTVar tv)
                printa tv fim i

-- Modulo que envia as mensagens
envia :: TVar String -> MVar Int -> [Int] -> IO()
envia tv fim [] = do
    i <- takeMVar fim
    putMVar fim (i-1)
envia tv fim (x:xa) = do
    putStrLn $ "Ping: " ++ show x
    atomically $ do
        writeTVar tv (show x)
    atomically (writeTVar tv (show x))
    envia tv fim xa

-- Modulo que espera as mensagens serem enviadas e recebidas
waitThreads :: MVar Int -> IO ()
waitThreads fim = do
    f <- takeMVar fim
    check fim f
        where
            check fim 0 = return ()
            check fim num = do
                putMVar fim num
                waitThreads fim

-- Modulo principal
main :: IO ()
main = do
    fim <- newMVar 2
    tv <- atomically(do{t <- newTVar "-1";
        readTVar t;
        return t;
    })
    forkIO (envia tv fim (reverse [(-5)..10]));
    forkIO (recebe tv fim);
    waitThreads fim
    return ()