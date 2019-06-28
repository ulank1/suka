package kg.docplus.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import kg.docplus.R;
import kg.docplus.model.Slider;

import java.util.ArrayList;

public class PageAdapter extends PagerAdapter {

    private ArrayList<Slider> images;
    private LayoutInflater inflater;
    private Context context;

    private OnClickListener listener;

    public interface OnClickListener {
        void onClick(int pos);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public PageAdapter(Context context, ArrayList<Slider> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        Slider slider = images.get(position);
        ImageView myImage = myImageLayout.findViewById(R.id.image);
        ImageView playButton = myImageLayout.findViewById(R.id.play_button);
        YouTubePlayerView youTubePlayerView = myImageLayout.findViewById(R.id.youtube_player_view);
        youTubePlayerView.setVisibility(View.GONE);
        youTubePlayerView.getPlayerUIController().showFullscreenButton(false);

        if (slider.isVideo()) {
            playButton.setVisibility(View.VISIBLE);
            Glide.with(context).load("https://i.ytimg.com/vi/" + slider.getYoutube_id() + "/mqdefault.jpg").into(myImage);
        } else {
            playButton.setVisibility(View.GONE);
            Glide.with(context).load(slider.getImage()).into(myImage);
        }

        playButton.setOnClickListener(v -> {
            youTubePlayerView.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.GONE);
            youTubePlayerView.initialize(initializedYouTubePlayer -> initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    initializedYouTubePlayer.loadVideo(slider.getYoutube_id(), 0);
                }
            }), true);
        });

        myImage.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(position);
            }
        });
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}