
package com.cspack.tweety.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.parceler.Generated;
import org.parceler.ParcelWrapper;
import org.parceler.ParcelerRuntimeException;

@Generated(value = "org.parceler.ParcelAnnotationProcessor", date = "2016-11-06T01:28-0800")
@SuppressWarnings({
    "unchecked",
    "deprecation"
})
public class UserModel$$Parcelable
    implements Parcelable, ParcelWrapper<com.cspack.tweety.models.UserModel>
{

    private com.cspack.tweety.models.UserModel userModel$$0;
    @SuppressWarnings("UnusedDeclaration")
    public final static UserModel$$Parcelable.Creator$$0 CREATOR = new UserModel$$Parcelable.Creator$$0();

    public UserModel$$Parcelable(com.cspack.tweety.models.UserModel userModel$$2) {
        userModel$$0 = userModel$$2;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel$$0, int flags) {
        write(userModel$$0, parcel$$0, flags, new HashSet<Integer>());
    }

    public static void write(com.cspack.tweety.models.UserModel userModel$$1, android.os.Parcel parcel$$1, int flags$$0, Set<Integer> identitySet$$0) {
        int identity$$0 = System.identityHashCode(userModel$$1);
        parcel$$1 .writeInt(identity$$0);
        if (!identitySet$$0 .contains(identity$$0)) {
            identitySet$$0 .add(identity$$0);
            if (userModel$$1 == null) {
                parcel$$1 .writeInt(-1);
            } else {
                parcel$$1 .writeInt(1);
                parcel$$1 .writeInt(userModel$$1 .friendCount);
                parcel$$1 .writeString(userModel$$1 .profileBackgroundImageUrlHttps);
                parcel$$1 .writeString(userModel$$1 .tagLine);
                parcel$$1 .writeString(userModel$$1 .profileBackgroundColor);
                parcel$$1 .writeInt((userModel$$1 .verified? 1 : 0));
                parcel$$1 .writeString(userModel$$1 .profileTextColor);
                parcel$$1 .writeString(userModel$$1 .screenName);
                parcel$$1 .writeInt((userModel$$1 .following? 1 : 0));
                parcel$$1 .writeString(userModel$$1 .name);
                parcel$$1 .writeString(userModel$$1 .id);
                parcel$$1 .writeString(userModel$$1 .profileImageUrl);
                parcel$$1 .writeInt(userModel$$1 .followerCount);
                parcel$$1 .writeInt(userModel$$1 .favoriteCount);
                parcel$$1 .writeInt((userModel$$1 .followRequestSent? 1 : 0));
            }
        }
    }

    @Override
    public int describeContents() {
        return  0;
    }

    @Override
    public com.cspack.tweety.models.UserModel getParcel() {
        return userModel$$0;
    }

    public static com.cspack.tweety.models.UserModel read(android.os.Parcel parcel$$3, Map<Integer, Object> identityMap$$0) {
        com.cspack.tweety.models.UserModel userModel$$3;
        int identity$$1 = parcel$$3 .readInt();
        if (identityMap$$0 .containsKey(identity$$1)) {
            com.cspack.tweety.models.UserModel userModel$$4 = ((com.cspack.tweety.models.UserModel) identityMap$$0 .get(identity$$1));
            if ((userModel$$4 == null)&&(identity$$1 != 0)) {
                throw new ParcelerRuntimeException("An instance loop was detected whild building Parcelable and deseralization cannot continue.  This error is most likely due to using @ParcelConstructor or @ParcelFactory.");
            }
            return userModel$$4;
        }
        if (parcel$$3 .readInt() == -1) {
            userModel$$3 = null;
            identityMap$$0 .put(identity$$1, null);
        } else {
            com.cspack.tweety.models.UserModel userModel$$5;
            identityMap$$0 .put(identity$$1, null);
            userModel$$5 = new com.cspack.tweety.models.UserModel();
            identityMap$$0 .put(identity$$1, userModel$$5);
            userModel$$5 .friendCount = parcel$$3 .readInt();
            userModel$$5 .profileBackgroundImageUrlHttps = parcel$$3 .readString();
            userModel$$5 .tagLine = parcel$$3 .readString();
            userModel$$5 .profileBackgroundColor = parcel$$3 .readString();
            userModel$$5 .verified = (parcel$$3 .readInt() == 1);
            userModel$$5 .profileTextColor = parcel$$3 .readString();
            userModel$$5 .screenName = parcel$$3 .readString();
            userModel$$5 .following = (parcel$$3 .readInt() == 1);
            userModel$$5 .name = parcel$$3 .readString();
            userModel$$5 .id = parcel$$3 .readString();
            userModel$$5 .profileImageUrl = parcel$$3 .readString();
            userModel$$5 .followerCount = parcel$$3 .readInt();
            userModel$$5 .favoriteCount = parcel$$3 .readInt();
            userModel$$5 .followRequestSent = (parcel$$3 .readInt() == 1);
            userModel$$3 = userModel$$5;
        }
        return userModel$$3;
    }

    public final static class Creator$$0
        implements Creator<UserModel$$Parcelable>
    {


        @Override
        public UserModel$$Parcelable createFromParcel(android.os.Parcel parcel$$2) {
            return new UserModel$$Parcelable(read(parcel$$2, new HashMap<Integer, Object>()));
        }

        @Override
        public UserModel$$Parcelable[] newArray(int size) {
            return new UserModel$$Parcelable[size] ;
        }

    }

}
