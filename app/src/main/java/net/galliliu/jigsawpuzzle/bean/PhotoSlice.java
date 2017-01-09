package net.galliliu.jigsawpuzzle.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by galliliu on 16/5/23.
 */
public class PhotoSlice implements Parcelable {
    private Bitmap photo;
    private int number;

    public PhotoSlice() {

    }

    public PhotoSlice(Bitmap bitmap, int num) {
        this.photo = bitmap;
        this.number = num;
    }

    protected PhotoSlice(Parcel in) {
        photo = in.readParcelable(Bitmap.class.getClassLoader());
        number = in.readInt();
    }

    public Bitmap getPhoto(){
        return photo;
    }

    public int getNumber() {
        return number;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(photo, flags);
        dest.writeInt(number);
    }

    public static final Creator<PhotoSlice> CREATOR = new Creator<PhotoSlice>() {
        @Override
        public PhotoSlice createFromParcel(Parcel in) {
            return new PhotoSlice(in);
        }

        @Override
        public PhotoSlice[] newArray(int size) {
            return new PhotoSlice[size];
        }
    };
}
