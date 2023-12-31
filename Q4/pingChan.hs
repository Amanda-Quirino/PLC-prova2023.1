import Control.Concurrent

-- Modulo que envia as mensagens
envia :: Chan String -> MVar Int -> [Int] -> IO ()
envia mv fim (x:xa) = do
    putStrLn $ "Ping: " ++ show x
    writeChan mv (show x)
    envia mv fim xa
envia mv fim [] = do
    i <- takeMVar fim
    putMVar fim (i-1)

-- Modulo que recebe as mensagens
recebe :: Chan String -> MVar Int -> IO ()
recebe mv fim = do 
    nu <- readChan mv
    rcv mv fim nu
    where
        rcv mv fim "0" = do
            i <- takeMVar fim
            putMVar fim (i - 1)
        rcv mv fim num = do
            putStrLn $ "Pong: " ++ num
            n <- readChan mv
            rcv mv fim n

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

-- Modulo principal que fara tudo rodar
main :: IO ()
main = do
    fim <- newMVar 2
    ch <- newChan
    mv <- newEmptyMVar
    i <- readLn
    forkIO (envia ch fim (reverse [0..i]))
    forkIO (recebe ch fim)
    waitThreads fim
    return ()
