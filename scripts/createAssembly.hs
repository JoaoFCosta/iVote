import System.Random
import Data.List (zip4)

main = do

  let parishes = ["Gualtar","Parque das Nações","Campolide","Mouros","São João","Grândola","São Vicente","São Lázaro","Santa Maria","Feira","Bandidos","Alvalade","Santo Tirso","Santidade","São Demagogo","Dão","Dona","Balte","Malte","Ranhoso","Campolide","Mouros","São João","Grândola"]
  let legis = legislativeParishesByCircle parishes
  let presis = presidentialParishesByCircle parishes

  votes <- createVotes (length legis + length presis)

  let inserts = zip4 [1..312] (legis ++ presis) votes (reverse votes)

  putStrLn $ unlines . map parseInsert $ inserts

-- (id, nome, idRonda/idCirculo, votoBranco, votoNulo)
parseInsert (a,(b,c),d,e) =
  let idAssembly = show a
      parish = b
      idParent = show c
      voteBlank = show d
      voteNull = show e
  in "(" ++ idAssembly ++ ", '" ++ parish ++ "', " ++ idParent ++ ", " ++ voteBlank ++ ", " ++ voteNull ++ "),"

createVotes :: Int -> IO [Integer]
createVotes n = do
  g <- newStdGen
  let votes = take n $ randomRs (0,5) g
  return votes

legislativeParishesByCircle parishes =
  let circlesID = zip [1,7..31] [6,12..36]
      circlesIDLists = map (\ (nMin, nMax) -> [nMin..nMax]) circlesID
      parishesByCircle = map (parishesFromCircleToCircle parishes) circlesIDLists
  in concat parishesByCircle

presidentialParishesByCircle parishes =
  let circlesIDLists = map (\ x -> [x]) [1..7]
      parishesByCircle = map (parishesFromCircleToCircle parishes) circlesIDLists
  in concat parishesByCircle

parishesFromCircleToCircle parishes circlesID = zip parishes (cycle circlesID)

