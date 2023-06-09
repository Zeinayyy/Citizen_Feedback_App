// Generated by view binder compiler. Do not edit!
package com.bangkit.citisnap.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public final class LayoutItemPostBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageView actionDown;

  @NonNull
  public final ImageView actionUp;

  @NonNull
  public final ConstraintLayout card;

  @NonNull
  public final ImageView comment;

  @NonNull
  public final TextView desc;

  @NonNull
  public final RelativeLayout iconGroup;

  @NonNull
  public final ImageView image;

  @NonNull
  public final CardView imageCard;

  @NonNull
  public final TextView name;

  @NonNull
  public final CircleImageView profileImage;

  @NonNull
  public final RelativeLayout profileInfo;

  @NonNull
  public final RelativeLayout rlUrgency;

  @NonNull
  public final TextView urgency;

  @NonNull
  public final TextView voteUpCount;

  private LayoutItemPostBinding(@NonNull RelativeLayout rootView, @NonNull ImageView actionDown,
      @NonNull ImageView actionUp, @NonNull ConstraintLayout card, @NonNull ImageView comment,
      @NonNull TextView desc, @NonNull RelativeLayout iconGroup, @NonNull ImageView image,
      @NonNull CardView imageCard, @NonNull TextView name, @NonNull CircleImageView profileImage,
      @NonNull RelativeLayout profileInfo, @NonNull RelativeLayout rlUrgency,
      @NonNull TextView urgency, @NonNull TextView voteUpCount) {
    this.rootView = rootView;
    this.actionDown = actionDown;
    this.actionUp = actionUp;
    this.card = card;
    this.comment = comment;
    this.desc = desc;
    this.iconGroup = iconGroup;
    this.image = image;
    this.imageCard = imageCard;
    this.name = name;
    this.profileImage = profileImage;
    this.profileInfo = profileInfo;
    this.rlUrgency = rlUrgency;
    this.urgency = urgency;
    this.voteUpCount = voteUpCount;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutItemPostBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutItemPostBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_item_post, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutItemPostBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.actionDown;
      ImageView actionDown = ViewBindings.findChildViewById(rootView, id);
      if (actionDown == null) {
        break missingId;
      }

      id = R.id.actionUp;
      ImageView actionUp = ViewBindings.findChildViewById(rootView, id);
      if (actionUp == null) {
        break missingId;
      }

      id = R.id.card;
      ConstraintLayout card = ViewBindings.findChildViewById(rootView, id);
      if (card == null) {
        break missingId;
      }

      id = R.id.comment;
      ImageView comment = ViewBindings.findChildViewById(rootView, id);
      if (comment == null) {
        break missingId;
      }

      id = R.id.desc;
      TextView desc = ViewBindings.findChildViewById(rootView, id);
      if (desc == null) {
        break missingId;
      }

      id = R.id.icon_group;
      RelativeLayout iconGroup = ViewBindings.findChildViewById(rootView, id);
      if (iconGroup == null) {
        break missingId;
      }

      id = R.id.image;
      ImageView image = ViewBindings.findChildViewById(rootView, id);
      if (image == null) {
        break missingId;
      }

      id = R.id.imageCard;
      CardView imageCard = ViewBindings.findChildViewById(rootView, id);
      if (imageCard == null) {
        break missingId;
      }

      id = R.id.name;
      TextView name = ViewBindings.findChildViewById(rootView, id);
      if (name == null) {
        break missingId;
      }

      id = R.id.profileImage;
      CircleImageView profileImage = ViewBindings.findChildViewById(rootView, id);
      if (profileImage == null) {
        break missingId;
      }

      id = R.id.profileInfo;
      RelativeLayout profileInfo = ViewBindings.findChildViewById(rootView, id);
      if (profileInfo == null) {
        break missingId;
      }

      id = R.id.rlUrgency;
      RelativeLayout rlUrgency = ViewBindings.findChildViewById(rootView, id);
      if (rlUrgency == null) {
        break missingId;
      }

      id = R.id.urgency;
      TextView urgency = ViewBindings.findChildViewById(rootView, id);
      if (urgency == null) {
        break missingId;
      }

      id = R.id.vote_up_count;
      TextView voteUpCount = ViewBindings.findChildViewById(rootView, id);
      if (voteUpCount == null) {
        break missingId;
      }

      return new LayoutItemPostBinding((RelativeLayout) rootView, actionDown, actionUp, card,
          comment, desc, iconGroup, image, imageCard, name, profileImage, profileInfo, rlUrgency,
          urgency, voteUpCount);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
