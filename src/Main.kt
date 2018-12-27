import java.awt.Color
import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage


class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val pixelSize = 8
            val path = "samples/car.jpg"
            val file = File(path)
            if(file.exists()) {
                val img = ImageIO.read(file)

                val toWrite = BufferedImage((img.width), (img.height), BufferedImage.TYPE_4BYTE_ABGR)

                for(x in 0 until img.width step pixelSize) {
                    for(y in 0 until img.height step pixelSize) {
                        val averageColor = averageColor(img, x, y, pixelSize, pixelSize)
                        val colorAsInt = getIntFromColor(averageColor.red, averageColor.green, averageColor.blue)

                        for(x1 in x until x + pixelSize) {
                            for(y1 in y until y + pixelSize) {
                                if(x1 < toWrite.width && y1 < toWrite.height) {
                                    toWrite.setRGB(x1, y1, colorAsInt)
                                }
                            }
                        }

                    }
                }

                val outputFile = File("out/car.png")
                ImageIO.write(toWrite, "png", outputFile)
            } else {
                print("boo")
            }
        }

        private fun getIntFromColor(r: Int, g: Int, b: Int): Int {
            var red = r
            var green = g
            var blue = b
            red = red shl 16 and 0x00FF0000 //Shift red 16-bits and mask out other stuff
            green = green shl 8 and 0x0000FF00 //Shift Green 8-bits and mask out other stuff
            blue = blue and 0x000000FF //Mask out anything not blue.

            return -0x1000000 or red or green or blue //0xFF000000 for 100% Alpha. Bitwise OR everything together.
        }

        private fun averageColor(
            img: BufferedImage, x0: Int, y0: Int, w: Int,
            h: Int
        ): Color {
            val x1 = x0 + w
            val y1 = y0 + h
            var sumRed: Long = 0
            var sumGreen: Long = 0
            var sumBlue: Long = 0

            if(x1 > img.width || y1 > img.height) {
                return Color.BLACK
            }

            for (x in x0 until x1) {
                for (y in y0 until y1) {
                    val pixel = Color(img.getRGB(x, y))
                    sumRed += pixel.red.toLong()
                    sumGreen += pixel.green.toLong()
                    sumBlue += pixel.blue.toLong()
                }
            }
            val num = w * h
            return Color((sumRed / num).toInt(), (sumGreen / num).toInt(), (sumBlue / num).toInt())
        }
    }
}

