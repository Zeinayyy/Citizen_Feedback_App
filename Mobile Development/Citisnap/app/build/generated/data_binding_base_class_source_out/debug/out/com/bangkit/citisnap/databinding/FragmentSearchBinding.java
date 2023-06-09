// Generated by view binder compiler. Do not edit!
package com.bangkit.citisnap.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.bangkit.citisnap.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentSearchBinding implements ViewBinding {
  @NonNull
  private final CoordinatorLayout rootView;

  @NonNull
  public final RecyclerView recycle;

  @NonNull
  public final EditText search;

  @NonNull
  public final SwipeRefreshLayout swipeRefresh;

  private FragmentSearchBinding(@NonNull CoordinatorLayout rootView, @NonNull RecyclerView recycle,
      @NonNull EditText search, @NonNull SwipeRefreshLayout swipeRefresh) {
    this.rootView = rootView;
    this.recycle = recycle;
    this.search = search;
    this.swipeRefresh = swipeRefresh;
  }

  @Override
  @NonNull
  public CoordinatorLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentSearchBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentSearchBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_search, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentSearchBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.recycle;
      RecyclerView recycle = ViewBindings.findChildViewById(rootView, id);
      if (recycle == null) {
        break missingId;
      }

      id = R.id.search;
      EditText search = ViewBindings.findChildViewById(rootView, id);
      if (search == null) {
        break missingId;
      }

      id = R.id.swipeRefresh;
      SwipeRefreshLayout swipeRefresh = ViewBindings.findChildViewById(rootView, id);
      if (swipeRefresh == null) {
        break missingId;
      }

      return new FragmentSearchBinding((CoordinatorLayout) rootView, recycle, search, swipeRefresh);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
