import System.Random
import Control.Monad (replicateM)
import Data.List (zip4)

main = do
  let citizenIDBegin = 12345678
  let citizenIDEnd = 12350678
  let range = citizenIDEnd - citizenIDBegin
  passwords <- createPasswords range
  names <- createNames range

  let inserts = zip4 [citizenIDBegin..citizenIDEnd] [1..range] passwords names

  putStrLn $ unlines . map parseInsert $ inserts

parseInsert (a,b,c,d) =
  let id = show a
      idVoter = show b
      password = c
      name = d
  in "(" ++ id ++ ", " ++ idVoter ++ ", '" ++ password ++ "', '" ++ name ++ "'),"

-- (id, idEleitor, password, nome)

createPasswords n = do
  passwords <- replicateM n $ do
    g <- newStdGen
    let size = head $ (randomRs (0,30) g)
    let password = take size $ (randomRs ('a','z') g)
    return password
  return passwords

createNames n = do
  let firstNames = ["José","Gustavo","Adelino","Martinho","Carlos","João","André","Tiago","Paulo","Rafael","Sara","Patricia","Joana","Teresa","Marta","Daniela","Ana","Maria","Diana","Andreia","Paula","Diogo","Inês","Catarina"]
  let lastNames = ["Gonçalves","Baptista","Valente","Henriques","Martinho","Aragão","Costa","Silva","Brandão","Almeida","Gomes","Pereira","Portas","Coelho","Sócrates","Cerqueira","Fernandes","Pinto","Mendes","Arantes","Paiva","Rodrigues","Pacheco","Soares"]
  names <- replicateM n $ do
    g <- newStdGen
    let random = take 2 $ (randomRs (0, 23) g)
    let firstName = firstNames !! (head random)
    let lastName = lastNames !! (last random)
    return (firstName ++ " " ++ lastName)
  return names

