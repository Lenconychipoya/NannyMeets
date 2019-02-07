package zw.co.appsareus.nannymeets.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ProfilePicture implements Parcelable {
    private String id;
    private String name;
    private String path;
    private String bucketReference;

    public static final Creator<ProfilePicture> CREATOR = new Creator<ProfilePicture>() {
        public ProfilePicture createFromParcel(Parcel source) {
            return new ProfilePicture(source);
        }

        public ProfilePicture[] newArray(int size) {
            return new ProfilePicture[size];
        }
    };

    public ProfilePicture() {
    }

    public ProfilePicture(String id, String name, String path, String bucketReference) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.bucketReference = bucketReference;
    }

    public ProfilePicture(String id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBucketReference() {
        return bucketReference;
    }

    public void setBucketReference(String bucketReference) {
        this.bucketReference = bucketReference;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ProfilePicture profilePicture = (ProfilePicture) o;
            return profilePicture.getPath().equalsIgnoreCase(this.getPath());
        } else {
            return false;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.path);
    }

    protected ProfilePicture(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.path = in.readString();
    }
}