import System.Random

main :: IO ()
main = do
  let votersID = votersIDByParish 12345678 1
  let size = length votersID

  ifVotedList <- randomIfVotedList size
  sections <- randomSections size

  let votersInfo = zip ifVotedList sections

  let voters = zip votersID votersInfo

  let insertsSQL = map buildInsertSQL voters

  putStrLn $ unlines insertsSQL

-- (idCidadao, idAssembleiaVoto, votou, seccao)
buildInsertSQL info =
  let citizen  = show . fst . fst $ info
      parish   = show . snd . fst $ info
      ifVoted  = show . fst . snd $ info
      sectionN = show . snd . snd $ info
  in "(" ++ citizen ++ ", " ++ parish ++ ", " ++ ifVoted ++ ", " ++ sectionN ++ "),"

votersIDByParish _ 26 = []
votersIDByParish citizenID parishID =
  let citizenRange = citizenID +200
      citizensID   = [citizenID..citizenRange -1]
      parishesID   = [parishID,parishID +24 .. 312]
  in combineLists citizensID parishesID ++ votersIDByParish citizenRange (parishID +1)

combineLists l1 l2 = concat . map (flip pairWithList l2) $ l1
pairWithList a = map (\x -> (a,x))

randomIfVotedList n = do
  g <- getStdGen
  let booleanList = take n (randoms g :: [Bool])
  return $ map (\x -> if x then 1 else 0) booleanList

randomSections :: Int -> IO [Int]
randomSections n = getStdGen >>= return . take n . randomRs (1, 10)


