package model;

import java.util.Date;


public class PhieuKham {
    private int MaPK;
    private int MaPhong;
    private int MaBN;
    private String NgayKham;

    public PhieuKham() {
    }

    public PhieuKham(int MaPK, int MaPhong, int MaBN, String NgayKham) {
        this.MaPK = MaPK;
        this.MaPhong = MaPhong;
        this.MaBN = MaBN;
        this.NgayKham = NgayKham;
    }

    public int getMaPK() {
        return MaPK;
    }

    public void setMaPK(int MaPK) {
        this.MaPK = MaPK;
    }

    public int getMaPhong() {
        return MaPhong;
    }

    public void setMaPhong(int MaPhong) {
        this.MaPhong = MaPhong;
    }

    public int getMaBN() {
        return MaBN;
    }

    public void setMaBN(int MaBN) {
        this.MaBN = MaBN;
    }

    public String getNgayKham() {
        return NgayKham;
    }

    public void setNgayKham(String NgayKham) {
        this.NgayKham = NgayKham;
    }
    
}


