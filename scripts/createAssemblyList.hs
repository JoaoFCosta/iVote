import System.Random
import Combine (combineLists)

main = do
  let list = [1..6]
  let assemblys = [1..144]
  let assemblyLists = combineLists list assemblys

  votes <- createVotes (length assemblyLists)

  let inserts = zip assemblyLists votes

  putStrLn $ unlines . map parseInsert $ inserts

-- (idLista, idAssembleiaVoto, votos)
parseInsert ((a,b),c) =
  let idList = show a
      idAssembly = show b
      vote = show c
  in "(" ++ idList ++ ", " ++ idAssembly ++ ", " ++ vote ++ "),"

createVotes :: Int -> IO [Integer]
createVotes n = do
  g <- newStdGen
  let votes = take n $ randomRs (0,200) g
  return votes
