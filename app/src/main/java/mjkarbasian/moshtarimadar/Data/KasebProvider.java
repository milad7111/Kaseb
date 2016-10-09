package mjkarbasian.moshtarimadar.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by family on 9/30/2016.
 */


public class KasebProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
    private static UriMatcher buildUriMatcher() {

        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = KasebContract.CONTENT_AUTHORITY;
//        uriMatcher.addURI(authority,KasebContract.PATH_CUSTOMERS ,MOVIE_POPULAR);
//        uriMatcher.addURI(authority,MovieContract.PATH_MOVIE+"/top_rated",MOVIE_TOP_RATED);
//        uriMatcher.addURI(authority,MovieContract.PATH_MOVIE+"/#",MOVIE_ID);
//        uriMatcher.addURI(authority,MovieContract.PATH_MOVIE+"/#"+"/trailer",MOVIE_ID_TRAILERS);
//        uriMatcher.addURI(authority, MovieContract.PATH_TRAILER,TRAILER);
        return uriMatcher;
    }
}
