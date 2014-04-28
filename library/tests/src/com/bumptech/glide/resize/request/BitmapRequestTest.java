package com.bumptech.glide.resize.request;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.test.AndroidTestCase;
import android.view.animation.Animation;
import com.bumptech.glide.loader.bitmap.BitmapLoadFactory;
import com.bumptech.glide.resize.target.Target;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BitmapRequestTest extends AndroidTestCase {

    public void testPlaceholderResourceIsSet() {
        final int expectedId = 12345;
        Drawable expected = new ColorDrawable(Color.RED);

        Context context = mockContextToReturn(expectedId, expected);
        MockTarget target = new MockTarget();

        BitmapRequest request = new BitmapRequestBuilder()
                .setContext(context)
                .setPlaceholderResource(expectedId)
                .setTarget(target)
                .build();
        request.run();

        assertEquals(expected, target.currentPlaceholder);
    }

    public void testPlaceholderDrawableIsSet() {
        Drawable expected = new ColorDrawable(Color.RED);

        MockTarget target = new MockTarget();

        BitmapRequest request = new BitmapRequestBuilder()
                .setContext(getContext())
                .setPlaceholderDrawable(expected)
                .setTarget(target)
                .build();
        request.run();

        assertEquals(expected, target.currentPlaceholder);
    }

    public void testErrorResourceIsSetOnLoadFailed() {
        final int expectedId = 12345;
        Drawable expected = new ColorDrawable(Color.RED);

        Context context = mockContextToReturn(expectedId, expected);
        MockTarget target = new MockTarget();

        BitmapRequest request = new BitmapRequestBuilder()
                .setContext(context)
                .setErrorResource(expectedId)
                .setTarget(target)
                .build();

        request.onLoadFailed(null);

        assertEquals(expected, target.currentPlaceholder);
    }

    public void testErrorDrawableIsSetOnLoadFailed() {
        Drawable expected = new ColorDrawable(Color.RED);

        MockTarget target = new MockTarget();

        BitmapRequest request = new BitmapRequestBuilder()
                .setContext(getContext())
                .setErrorDrawable(expected)
                .setTarget(target)
                .build();

        request.onLoadFailed(null);

        assertEquals(expected, target.currentPlaceholder);
    }

    public void setTestPlaceholderDrawableSetOnNullModel() {
        Drawable placeholder = new ColorDrawable(Color.RED);

        BitmapLoadFactory loadFactory = mockLoadFactory();

        MockTarget target = new MockTarget();

        BitmapRequest request = new BitmapRequestBuilder()
                .setContext(getContext())
                .setPlaceholderDrawable(placeholder)
                .setTarget(target)
                .setBitmapLoadFactory(loadFactory)
                .build();

        request.run();
        request.onSizeReady(10, 10);

        assertEquals(placeholder, target.currentPlaceholder);
    }

    public void testErrorDrawableSetOnNullModel() {
        Drawable placeholder = new ColorDrawable(Color.RED);
        Drawable errorPlaceholder = new ColorDrawable(Color.GREEN);

        BitmapLoadFactory loadFactory = mockLoadFactory();

        MockTarget target = new MockTarget();

        BitmapRequest request = new BitmapRequestBuilder()
                .setContext(getContext())
                .setPlaceholderDrawable(placeholder)
                .setErrorDrawable(errorPlaceholder)
                .setTarget(target)
                .setBitmapLoadFactory(loadFactory)
                .build();

        request.run();
        request.onSizeReady(10, 10);

        assertEquals(errorPlaceholder, target.currentPlaceholder);
    }

    private BitmapLoadFactory mockLoadFactory() {
        BitmapLoadFactory loadFactory = mock(BitmapLoadFactory.class);
        when(loadFactory.getLoadTask(anyObject(), anyInt(), anyInt())).thenReturn(null);
        return loadFactory;
    }

    private Context mockContextToReturn(int resourceId, Drawable drawable) {
        Resources resources = mock(Resources.class);
        Context context = mock(Context.class);

        when(context.getApplicationContext()).thenReturn(context);
        when(context.getResources()).thenReturn(resources);
        when(resources.getDrawable(eq(resourceId))).thenReturn(drawable);

        return context;
    }

    private static class MockTarget implements Target {
        private Drawable currentPlaceholder;

        @Override
        public void onImageReady(Bitmap bitmap) {
            currentPlaceholder = null;
        }

        @Override
        public void setPlaceholder(Drawable placeholder) {
            currentPlaceholder = placeholder;
        }

        @Override
        public void getSize(SizeReadyCallback cb) {
        }

        @Override
        public void startAnimation(Animation animation) {
        }

        @Override
        public void setRequest(Request request) {
        }

        @Override
        public Request getRequest() {
            return null;
        }
    }
}
