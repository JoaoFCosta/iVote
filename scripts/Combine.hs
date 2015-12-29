module Combine where

-- Combine each element of the first list with all of the second
combineLists l1 l2 = concat . map (flip pairWithList l2) $ l1
pairWithList a = map (\x -> (a,x))
