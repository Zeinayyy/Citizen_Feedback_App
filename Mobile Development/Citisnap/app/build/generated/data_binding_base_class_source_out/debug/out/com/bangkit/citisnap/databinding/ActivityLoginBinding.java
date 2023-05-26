// Generated by view binder compiler. Do not edit!
package com.bangkit.citisnap.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.bangkit.citisnap.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextInputEditText email;

  @NonNull
  public final TextInputLayout emailLayout;

  @NonNull
  public final Button googleSignIn;

  @NonNull
  public final LinearLayout linearLayout;

  @NonNull
  public final LinearLayout linearLayout2;

  @NonNull
  public final Button login;

  @NonNull
  public final RelativeLayout logo;

  @NonNull
  public final ImageView logoImg;

  @NonNull
  public final TextInputEditText password;

  @NonNull
  public final TextInputLayout passwordLayout;

  @NonNull
  public final TextView register;

  @NonNull
  public final TextView textView;

  private ActivityLoginBinding(@NonNull ConstraintLayout rootView, @NonNull TextInputEditText email,
      @NonNull TextInputLayout emailLayout, @NonNull Button googleSignIn,
      @NonNull LinearLayout linearLayout, @NonNull LinearLayout linearLayout2,
      @NonNull Button login, @NonNull RelativeLayout logo, @NonNull ImageView logoImg,
      @NonNull TextInputEditText password, @NonNull TextInputLayout passwordLayout,
      @NonNull TextView register, @NonNull TextView textView) {
    this.rootView = rootView;
    this.email = email;
    this.emailLayout = emailLayout;
    this.googleSignIn = googleSignIn;
    this.linearLayout = linearLayout;
    this.linearLayout2 = linearLayout2;
    this.login = login;
    this.logo = logo;
    this.logoImg = logoImg;
    this.password = password;
    this.passwordLayout = passwordLayout;
    this.register = register;
    this.textView = textView;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.email;
      TextInputEditText email = ViewBindings.findChildViewById(rootView, id);
      if (email == null) {
        break missingId;
      }

      id = R.id.emailLayout;
      TextInputLayout emailLayout = ViewBindings.findChildViewById(rootView, id);
      if (emailLayout == null) {
        break missingId;
      }

      id = R.id.googleSignIn;
      Button googleSignIn = ViewBindings.findChildViewById(rootView, id);
      if (googleSignIn == null) {
        break missingId;
      }

      id = R.id.linearLayout;
      LinearLayout linearLayout = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout == null) {
        break missingId;
      }

      id = R.id.linearLayout2;
      LinearLayout linearLayout2 = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout2 == null) {
        break missingId;
      }

      id = R.id.login;
      Button login = ViewBindings.findChildViewById(rootView, id);
      if (login == null) {
        break missingId;
      }

      id = R.id.logo;
      RelativeLayout logo = ViewBindings.findChildViewById(rootView, id);
      if (logo == null) {
        break missingId;
      }

      id = R.id.logoImg;
      ImageView logoImg = ViewBindings.findChildViewById(rootView, id);
      if (logoImg == null) {
        break missingId;
      }

      id = R.id.password;
      TextInputEditText password = ViewBindings.findChildViewById(rootView, id);
      if (password == null) {
        break missingId;
      }

      id = R.id.passwordLayout;
      TextInputLayout passwordLayout = ViewBindings.findChildViewById(rootView, id);
      if (passwordLayout == null) {
        break missingId;
      }

      id = R.id.register;
      TextView register = ViewBindings.findChildViewById(rootView, id);
      if (register == null) {
        break missingId;
      }

      id = R.id.textView;
      TextView textView = ViewBindings.findChildViewById(rootView, id);
      if (textView == null) {
        break missingId;
      }

      return new ActivityLoginBinding((ConstraintLayout) rootView, email, emailLayout, googleSignIn,
          linearLayout, linearLayout2, login, logo, logoImg, password, passwordLayout, register,
          textView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
