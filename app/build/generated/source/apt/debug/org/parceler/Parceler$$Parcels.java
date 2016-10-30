
package org.parceler;

import java.util.HashMap;
import java.util.Map;
import com.cspack.tweety.models.UserModel;
import com.cspack.tweety.models.UserModel$$Parcelable;

@Generated(value = "org.parceler.ParcelAnnotationProcessor", date = "2016-10-30T13:21-0700")
@SuppressWarnings({
    "unchecked",
    "deprecation"
})
public class Parceler$$Parcels
    implements Repository<org.parceler.Parcels.ParcelableFactory>
{

    private final Map<Class, org.parceler.Parcels.ParcelableFactory> map$$0 = new HashMap<Class, org.parceler.Parcels.ParcelableFactory>();

    public Parceler$$Parcels() {
        map$$0 .put(UserModel.class, new Parceler$$Parcels.UserModel$$Parcelable$$0());
    }

    public Map<Class, org.parceler.Parcels.ParcelableFactory> get() {
        return map$$0;
    }

    private final static class UserModel$$Parcelable$$0
        implements org.parceler.Parcels.ParcelableFactory<UserModel>
    {


        @Override
        public UserModel$$Parcelable buildParcelable(UserModel input) {
            return new UserModel$$Parcelable(input);
        }

    }

}
