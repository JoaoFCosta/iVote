import System.Random
import Control.Monad (replicateM)
import Combine

main = do
  let assemblys = [145..312]
  let candidate = [12345678..12345683]
  let assemblyLists = combineLists assemblys candidate

  votes <- createVotes (length assemblyLists)

  let inserts = zip  assemblyLists votes

  putStrLn $ unlines . map parseInsert $ inserts

-- (idLista, idAssembleiaVoto, votos)
parseInsert ((a,b),c) =
  let idCandidate = show a
      idAssembly = show b
      vote = show c
  in "(" ++ idCandidate ++ ", " ++ idAssembly ++ ", " ++ vote ++ "),"

createVotes :: Int -> IO [Int]
createVotes n = do
  g <- newStdGen
  let vote = take n $ (randomRs (0,200) g)
  return vote
