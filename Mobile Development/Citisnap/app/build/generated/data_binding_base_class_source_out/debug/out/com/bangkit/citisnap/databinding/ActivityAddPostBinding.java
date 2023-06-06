// Generated by view binder compiler. Do not edit!
package com.bangkit.citisnap.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.bangkit.citisnap.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityAddPostBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView back;

  @NonNull
  public final ImageView camera;

  @NonNull
  public final CardView cardView;

  @NonNull
  public final EditText description;

  @NonNull
  public final ImageView gallery;

  @NonNull
  public final ImageView location;

  @NonNull
  public final ImageView photo;

  @NonNull
  public final CircleImageView photoProfile;

  @NonNull
  public final Button post;

  @NonNull
  public final ProgressBar progressBar;

  @NonNull
  public final RelativeLayout relativeLayout2;

  private ActivityAddPostBinding(@NonNull ConstraintLayout rootView, @NonNull ImageView back,
      @NonNull ImageView camera, @NonNull CardView cardView, @NonNull EditText description,
      @NonNull ImageView gallery, @NonNull ImageView location, @NonNull ImageView photo,
      @NonNull CircleImageView photoProfile, @NonNull Button post, @NonNull ProgressBar progressBar,
      @NonNull RelativeLayout relativeLayout2) {
    this.rootView = rootView;
    this.back = back;
    this.camera = camera;
    this.cardView = cardView;
    this.description = description;
    this.gallery = gallery;
    this.location = location;
    this.photo = photo;
    this.photoProfile = photoProfile;
    this.post = post;
    this.progressBar = progressBar;
    this.relativeLayout2 = relativeLayout2;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAddPostBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAddPostBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_add_post, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAddPostBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.back;
      ImageView back = ViewBindings.findChildViewById(rootView, id);
      if (back == null) {
        break missingId;
      }

      id = R.id.camera;
      ImageView camera = ViewBindings.findChildViewById(rootView, id);
      if (camera == null) {
        break missingId;
      }

      id = R.id.cardView;
      CardView cardView = ViewBindings.findChildViewById(rootView, id);
      if (cardView == null) {
        break missingId;
      }

      id = R.id.description;
      EditText description = ViewBindings.findChildViewById(rootView, id);
      if (description == null) {
        break missingId;
      }

      id = R.id.gallery;
      ImageView gallery = ViewBindings.findChildViewById(rootView, id);
      if (gallery == null) {
        break missingId;
      }

      id = R.id.location;
      ImageView location = ViewBindings.findChildViewById(rootView, id);
      if (location == null) {
        break missingId;
      }

      id = R.id.photo;
      ImageView photo = ViewBindings.findChildViewById(rootView, id);
      if (photo == null) {
        break missingId;
      }

      id = R.id.photoProfile;
      CircleImageView photoProfile = ViewBindings.findChildViewById(rootView, id);
      if (photoProfile == null) {
        break missingId;
      }

      id = R.id.post;
      Button post = ViewBindings.findChildViewById(rootView, id);
      if (post == null) {
        break missingId;
      }

      id = R.id.progressBar;
      ProgressBar progressBar = ViewBindings.findChildViewById(rootView, id);
      if (progressBar == null) {
        break missingId;
      }

      id = R.id.relativeLayout2;
      RelativeLayout relativeLayout2 = ViewBindings.findChildViewById(rootView, id);
      if (relativeLayout2 == null) {
        break missingId;
      }

      return new ActivityAddPostBinding((ConstraintLayout) rootView, back, camera, cardView,
          description, gallery, location, photo, photoProfile, post, progressBar, relativeLayout2);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
