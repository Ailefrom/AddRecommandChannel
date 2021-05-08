package com.ailefrom.tvprovider.channel;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.tvprovider.media.tv.Channel;
import androidx.tvprovider.media.tv.ChannelLogoUtils;
import androidx.tvprovider.media.tv.PreviewProgram;
import androidx.tvprovider.media.tv.TvContractCompat;

public class TvProviderChannel extends Activity {
    private static final String TAG = "TvProviderChannel";
    private TextView mAddChannel, mShowChannelNum;
    private int ContentNum = 0;
    private final String PACKAGE_NAME = "com.ailefrom.tvprovider.channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_provider_channel);

        mAddChannel = (TextView) findViewById(R.id.AddChannelAction);
        mAddChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddChannelRow();
                mShowChannelNum.setText("Add Channel " + ContentNum);
            }
        });
        mShowChannelNum = (TextView) findViewById(R.id.ShowChannelNumber);
    }

    private void AddChannelRow()
    {
        Channel channel = new Channel.Builder()
                .setDisplayName("Ailefrom")
                .setDescription("Ailefrom")
                .setDisplayNumber("Ailefrom_NUMBER")
                .setType(TvContractCompat.Channels.TYPE_PREVIEW)
                .setInputId("Ailefrom_123")
                .setOriginalNetworkId(1)
                .build();

        Uri channelUri = getContentResolver().insert(TvContractCompat.Channels.CONTENT_URI, channel.toContentValues());

        Log.d(TAG, "AddChannelContent:" + channel.toString() +" channelUri ["+channelUri.toString() +"]");
        AddChannelLogo(channelUri);
        AddChannelContent(channelUri);
    }

    private void AddChannelLogo(Uri channelUri)
    {
        long UriId = ContentUris.parseId(channelUri);
        Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.lb_ic_play);
        ChannelLogoUtils.storeChannelLogo(this, UriId, bm);
    }

    private void AddChannelContent(Uri channelUri)
    {
        Uri logoUri = Uri.parse("android.resource://" + PACKAGE_NAME + "/" + R.drawable.lb_ic_loop);
        PreviewProgram content = new PreviewProgram.Builder()
                .setChannelId(ContentUris.parseId(channelUri))
                .setType(TvContractCompat.PreviewPrograms.TYPE_TV_SERIES)
                .setTitle("Ailefrom Program_"+ContentNum)
                .setDescription("Ailefrom Program_Descripton_"+ContentNum)
                .setAuthor("Ailefrom")
                .setIntent(SetIntent())
                .setPosterArtUri(logoUri)
                .build();
        ContentNum++;
        Uri contentUri = getContentResolver().insert(TvContractCompat.PreviewPrograms.CONTENT_URI, content.toContentValues());

        Log.d(TAG, "AddChannelContent:" + content.toString() +" channelUri ["+contentUri.toString() +"]");
    }

    private Intent SetIntent()
    {
        Intent intent = new Intent();
        intent.setClass(this, TvProviderChannel.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

}