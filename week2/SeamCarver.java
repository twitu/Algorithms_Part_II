import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.lang.Math;

public class SeamCarver {
    private Picture picture;
    private double[][] energyarray;
    private boolean istransposed;
    private boolean isHorizontalSeamCall;

    public SeamCarver (Picture picture) {
        this.picture = picture;
        this.energyarray = energyMatrix();
        this.isHorizontalSeamCall = false;
        this.istransposed = false;
    }

    private double[][] energyMatrix() {
        // TODO: Keep ordering in mind when time trials fail, currently row-major traversal of pixels.
        double[][] energyarray = new double[this.height()][this.width()];
        for (int j=0; j<this.height(); j++) {
            for (int i = 0; i < this.width(); i++) {
                energyarray[j][i] = energy(i, j);
            }
        }
        return energyarray;
    }

    public double energy(int x, int y) {
        if (x < 0 || x >=this.width() || y < 0 || y >= this.height()) throw new IllegalArgumentException("Invalid energy call.");
        if (x==0 || x==this.width()-1 || y==0 || y==this.height()-1) return 1000;
        int rgbleft = this.picture.getRGB(x-1, y);
        int rgbright = this.picture.getRGB(x+1, y);
        int rgbup = this.picture.getRGB(x, y-1);
        int rgbdown = this.picture.getRGB(x, y+1);

        int rdx = ((rgbleft >> 16) & 0xFF) - ((rgbright >> 16) & 0xFF);
        int gdx = ((rgbleft >> 8) & 0xFF) - ((rgbright >> 8) & 0xFF);
        int bdx = ((rgbleft) & 0xFF) - ((rgbright) & 0xFF);

        int rdy = ((rgbup >> 16) & 0xFF) - ((rgbdown >> 16) & 0xFF);
        int gdy = ((rgbup >> 8) & 0xFF) - ((rgbdown >> 8) & 0xFF);
        int bdy = ((rgbup) & 0xFF) - ((rgbdown) & 0xFF);

        int delx = rdx*rdx + gdx*gdx + bdx*bdx;
        int dely = rdy*rdy + gdy*gdy + bdy*bdy;

        return Math.sqrt(delx + dely);
    }

    public Picture picture() {
        return this.picture;
    }

    public int height() {
        return this.picture.height();
    }

    public int width() {
        return this.picture.width();
    }

    private boolean validSeam(int[] seam) {
        if (seam==null) return false;
        if (this.width() <= 1) return false;
        if (seam.length!=this.height()) return false;
        if (seam[0]<0 || seam[0] >= this.width()) return false;
        for (int i=1; i<seam.length; i++) {
            if ((seam[i-1]-seam[i])*(seam[i-1]-seam[i])>1) return false;
            if (seam[i]<0 || seam[i] >= this.width()) return false;
        }
        return true;

    }

    private void printMatrix(Double[][] matrix) {
        for (int i=0; i<matrix.length; i++) {
            for (int j=0; j<matrix[0].length; j++) {
                System.out.printf("%f\t", matrix[i][j]);
            }
            System.out.printf("\n");
        }
    }

    private void transposeImage() {
        Picture newpic = new Picture(this.height(), this.width());
        double[][] newenergyarray = new double[this.width()][this.height()];
        for (int i=0; i<this.width(); i++) {
            for (int j=0; j<this.height(); j++) {
                newpic.set(j, i, this.picture.get(i, j));
                newenergyarray[i][j] = this.energyarray[j][i];
            }
        }

        this.energyarray = newenergyarray;
        this.picture = newpic;
        if (this.istransposed) this.istransposed = false;
        else this.istransposed = true;

    }

    public int[] findVerticalSeam() {
        if (istransposed && !isHorizontalSeamCall) transposeImage();
        Double[][] tempenergyarray = new Double[this.height()][this.width()];
        for (int j=0; j<this.height(); j++) {
            for (int i=0; i<this.width(); i++) {
                if (j==0) tempenergyarray[j][i] = 1000.0;
                else tempenergyarray[j][i] = Double.MAX_VALUE;
            }
        }

        double sum1;
        double sum2;
        double sum3;

        for (int j=1; j<this.height(); j++) { // iterate form second row to end
            for (int i=0; i<this.width(); i++) { // iterate rows from border to border
                int a = i-1;
                int c = i+1;
                
                if (i>0) {
                    sum1 = energyarray[j][a] + tempenergyarray[j-1][i];
                    if (sum1 < tempenergyarray[j][a]) tempenergyarray[j][i-1] = sum1;
                }

                sum2 = energyarray[j][i] + tempenergyarray[j-1][i];
                if (sum2 < tempenergyarray[j][i]) tempenergyarray[j][i] = sum2;

                if (i<this.width()-1){
                    sum3 = energyarray[j][i+1] + tempenergyarray[j-1][i];
                    if (sum3 < tempenergyarray[j][i+1]) tempenergyarray[j][i+1] = sum3;
                }

            }
        }

        int minindex = 0;
        double min = Double.MAX_VALUE;
        int l = this.height()-1;
        for (int i = 1; i < this.width()-1; i++) {
            if (tempenergyarray[l][i] < min) {
                min = tempenergyarray[l][i];
                minindex = i;
            }
        }

        int[] verticalroute = new int[this.height()];
        verticalroute[this.height()-1] = minindex;
        int tempminindex = 0;
        for (int j=this.height()-2; j>-1; j--) {
            min = Double.MAX_VALUE;
            tempminindex = minindex;
            int a = tempminindex-1;
            int c = tempminindex+1;
            if (tempminindex > 0) {
                if (tempenergyarray[j][tempminindex-1] < min) {
                    min = tempenergyarray[j][tempminindex-1];
                    minindex = tempminindex - 1;
                }
            }

            if (tempenergyarray[j][tempminindex] < min) {
                min = tempenergyarray[j][tempminindex];
                minindex = tempminindex;
            }

            if (tempminindex < this.width()-1) {
                if (tempenergyarray[j][tempminindex+1] < min) {
                    min = tempenergyarray[j][tempminindex+1];
                    minindex = tempminindex + 1;
                }
            }

            verticalroute[j] = minindex;
        }
        return verticalroute;
    }

    public int[] findHorizontalSeam() {
        if (!this.istransposed) {
            transposeImage();
        }
        this.isHorizontalSeamCall = true;
        int[] seam = findVerticalSeam();
        this.isHorizontalSeamCall = false;
        transposeImage();
        this.istransposed = false;
        return seam;
    }

    public void removeVerticalSeam(int[] seam) {
        if (!validSeam(seam)) throw new IllegalArgumentException("Invalid seam");
        if (istransposed && !isHorizontalSeamCall) transposeImage();
        int j = 0;
        Picture newpic = new Picture(this.width()-1, this.height());
        double[][] newenergyarray = new double[this.height()][this.width()-1];
        for (int i: seam) {
            for (int k=0; k<newpic.width(); k++) {
                if (k<i) newpic.set(k, j, this.picture.get(k, j));
                else if (k>=i) newpic.set(k, j, this.picture.get(k+1, j));
            }
            System.arraycopy(energyarray[j], 0, newenergyarray[j], 0, i);
            System.arraycopy(energyarray[j], i+1, newenergyarray[j], i, this.width()-1-i);
            j++;
        }
        this.picture = newpic;
        this.energyarray = newenergyarray;

        j = 0;
        for (int i: seam) {
            if (i!=0) this.energyarray[j][i - 1] = energy(i - 1, j);
            if (i!=this.width()) this.energyarray[j][i] = energy(i, j);
        }
    }

    public void removeHorizontalSeam(int[] seam) {
        if (!istransposed) transposeImage();
        this.isHorizontalSeamCall = true;
        removeVerticalSeam(seam);
        this.isHorizontalSeamCall = false;
        transposeImage();
        istransposed = false;
    }

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());

        SeamCarver sc = new SeamCarver(picture);
        StdOut.println("The height is:"+sc.height());
        StdOut.println("The width is:"+sc.width());

        StdOut.printf("Printing energy calculated for each pixel.\n");

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.0f ", sc.energy(col, row));
            StdOut.println();
        }
    }
}
