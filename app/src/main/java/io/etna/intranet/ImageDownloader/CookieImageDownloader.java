package io.etna.intranet.ImageDownloader;

/**
 * Created by nextjoey on 19/04/2017.
 */
import android.content.Context;
import android.net.Uri;
import com.squareup.picasso.UrlConnectionDownloader;
import java.io.IOException;
import java.net.HttpURLConnection;

public  class CookieImageDownloader extends UrlConnectionDownloader{

    public CookieImageDownloader(Context context) {
        super(context);
    }

    @Override
    protected HttpURLConnection openConnection(Uri path) throws IOException{
        HttpURLConnection conn = super.openConnection(path);

        String cookieName = "authenticator";
        String cookieValue = "eyJpZGVudGl0eSI6ImV5SnBaQ0k2Tnprek5Dd2liRzluYVc0aU9pSmlaV1J0YVc1ZmFpSXNJbXh2WjJGeklqcG1ZV3h6WlN3aVozSnZkWEJ6SWpwYkluTjBkV1JsYm5RaVhTd2liRzluYVc1ZlpHRjBaU0k2SWpJd01UY3RNRFF0TVRrZ01UQTZNek02TVRVaWZRPT0iLCJzaWduYXR1cmUiOiJqQ1dFUnYyTFFGejBhSlwvMTBqMGNtcTZ3UXZcLzYwRkVEK1wvdXdzRlNiQ2wrRDRSUXpxUG9MaDJqQjlCcnJnWVZzSk5zMHVKckYrKzBYUkxCODJoZVF1V1pQaUtKd00xSVgyQ283a0dFUFpCVkJ6MlFOT2xGdWdON3B3cEpoQ1BjV0J1K3JaWGhZYkU5b1hFTkI5TVMwbjlNbzBSemxQV2hcL2VtNjlvNk9XR3ZyU1czQ3RYcW42QmtwYzVwV3dGcjY0NzlXeHl0dHpsZ1RLY21pZ3dBbXJKb2N1MXlwOWVFNFZBTUJkMUFxcHVIZHBOWlhqRnNiY3JHbnQ0VkVocmlUSkxucG9GVHBPWFZUeDBFcGRYZGM0d001aVhHZWFlYmYwMTA4V25vRVF1VGtlOFZqZXZva05OK2tyTFNxaVU2UzVWZDBOUEdHakdYVFc2UkppU0g5dUpaM3lpbGVXSU1NcVJ3MEE0aHFjRVhTMHRSdGNGbGxvT1RrdVkyM2JKTWordnJ3SWM1NVwvK0pxNzdmd3BURU9SbjI2aUJVQzc4UGM3V1FmYjQ5VUJjaCs0UUNzTTdBR0ozYk9lKzQ4S0dNcnZXamthN2dVcWpWNmFuU2VxclhKZVlWUTk1dVp2QUFmbVR0cTU3Rm0ydm1aVVZjaWdRWkNZaE05aXk1K0U4S05FZVBBYUdHNW5qdTRQRUZuXC92TXIrV293TklzMVgxdDBIQTd6TUlVUHB4WEIyaEpzSkFvd3hWdGtEZGlDNmZUd0k5NEVBeVlOajdMWHdSbkxSc05EenJjR0t3ckFENDJKbTlHNGhhZXF2N0crOVwvK2ZHRHJPZXJyQXVQb1Z2VHI1aEVPUmNxQ0t0TWRROWZRNW5aTkFEU25BYTRwSWhZNzM5dkFpQXE2dHFFdmM9In0";
        conn.setRequestProperty("Cookie",cookieName + "=" + cookieValue );

        return conn;
    }
}