import Control.Concurrent
import Control.Concurrent.STM

-- Modulo que recebe as mensagens
recebe :: TVar String -> MVar Int -> IO()
recebe tv fim = do
    x <- atomically (readTVar tv)
    putStrLn $ "Pong: " ++ x
    printa tv fim x
        where
            printa tv fim "0" = do
                i <- takeMVar fim
                putMVar fim (i-1)
            printa tv fim nu = do
                i <- atomically (do{t <- readTVar tv;
                writeTVar tv "-1";
                return t;
                })
                if (i /= "-1") then
                    do
                        putStrLn $ "Pong: " ++ i
                        printa tv fim i
                else
                    printa tv fim i

-- Modulo que envia as mensagens
envia :: TVar String -> MVar Int -> [Int] -> IO()
envia tv fim [] = do
    i <- takeMVar fim
    putMVar fim (i-1)
envia tv fim (x:xa) = do
    t <- atomically(readTVar tv)
    if (t == "-1") then
        do
            atomically (writeTVar tv (show x))
            putStrLn $ "Ping: " ++ show x
            envia tv fim xa
    else
        envia tv fim (x:xa)
        

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
    i <- readLn
    forkIO (envia tv fim (reverse [0..i]));
    forkIO (recebe tv fim);
    waitThreads fim
    return ()