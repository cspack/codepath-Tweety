
package org.parceler;

import java.util.HashMap;
import java.util.Map;
import com.cspack.tweety.models.TweetListModel;
import com.cspack.tweety.models.TweetListModel$$Parcelable;
import com.cspack.tweety.models.TweetModel;
import com.cspack.tweety.models.TweetModel$$Parcelable;
import com.cspack.tweety.models.UserModel;
import com.cspack.tweety.models.UserModel$$Parcelable;

@Generated(value = "org.parceler.ParcelAnnotationProcessor", date = "2016-11-06T15:59-0800")
@SuppressWarnings({
    "unchecked",
    "deprecation"
})
public class Parceler$$Parcels
    implements Repository<org.parceler.Parcels.ParcelableFactory>
{

    private final Map<Class, org.parceler.Parcels.ParcelableFactory> map$$0 = new HashMap<Class, org.parceler.Parcels.ParcelableFactory>();

    public Parceler$$Parcels() {
        map$$0 .put(TweetListModel.class, new Parceler$$Parcels.TweetListModel$$Parcelable$$0());
        map$$0 .put(UserModel.class, new Parceler$$Parcels.UserModel$$Parcelable$$0());
        map$$0 .put(TweetModel.class, new Parceler$$Parcels.TweetModel$$Parcelable$$0());
    }

    public Map<Class, org.parceler.Parcels.ParcelableFactory> get() {
        return map$$0;
    }

    private final static class TweetListModel$$Parcelable$$0
        implements org.parceler.Parcels.ParcelableFactory<TweetListModel>
    {


        @Override
        public TweetListModel$$Parcelable buildParcelable(TweetListModel input) {
            return new TweetListModel$$Parcelable(input);
        }

    }

    private final static class TweetModel$$Parcelable$$0
        implements org.parceler.Parcels.ParcelableFactory<TweetModel>
    {


        @Override
        public TweetModel$$Parcelable buildParcelable(TweetModel input) {
            return new TweetModel$$Parcelable(input);
        }

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
