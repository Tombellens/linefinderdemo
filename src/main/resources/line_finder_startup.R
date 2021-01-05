library(opencv)
library(magick)
library(image.binarization)
library(image.textlinedetector)
path  <- system.file(package = "image.textlinedetector", "extdata", "example.png")
img   <- image_read(path)
img   <- image_binarization(img, type = "su")
areas <- image_textlines_astar(img, morph = TRUE, step = 2, mfactor = 5)
areasareas <- lines(areas, img, channels = "bgr")
lines <- areas$n


