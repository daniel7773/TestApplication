package com.example.testapp.ui.auth;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.models.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.BiFunction;

public class AuthAdapter extends RecyclerView.Adapter<AuthAdapter.AbstractViewHolder> {

    public static final String TAG = "PracticeAdapter";
    AuthPresenter authPresenter;

    private List<ListItem> listItems = new ArrayList<>(0);
    Context context;

    public AuthAdapter(Context context, AuthPresenter authPresenter) {
        this.context = context;
        this.authPresenter = authPresenter;
    }

    public static abstract class AbstractViewHolder extends RecyclerView.ViewHolder {

        protected final AuthAdapter adapter;
        protected ListItem listItem;

        public AbstractViewHolder(View itemView, AuthAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
        }

        public void onBind(ListItem listItem) {
            this.listItem = listItem;
        }
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemType type = ListItemType.findById(viewType);
        try {
            return type.funcCreate.apply(LayoutInflater.from(parent.getContext()).inflate(type.layout, parent, false), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (listItems == null) {
            return 0;
        }
        return listItems.size();
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.onBind(listItems.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return listItems.get(position).type.ordinal();
    }

    public void setItems(List<User> users) {
        Log.d(TAG, "setItems in adapter called " + users.size());
        listItems.clear();
        for (User user : users) {
            listItems.add(ListItem.createUser(user));
        }
        notifyDataSetChanged();
    }

    public static class UserHolder extends AbstractViewHolder {

        TextView label;
        ListItem listItem;

        public UserHolder(View itemView, AuthAdapter adapter) {
            super(itemView, adapter);
            label = itemView.findViewById(R.id.user_name);
            itemView.setOnClickListener(v -> adapter.authPresenter.logIn(listItem.user.getId()));
        }

        @Override
        public void onBind(ListItem listItem) {
            super.onBind(listItem);
            this.listItem = listItem;
            label.setText(listItem.user.getUsernameSafe());
        }
    }

    public enum ListItemType {
        USER(R.layout.auth_li_user, UserHolder::new),
        ;

        public final int id;
        public final int layout;
        public final BiFunction<View, AuthAdapter, AbstractViewHolder> funcCreate;

        ListItemType(int layout, BiFunction<View, AuthAdapter, AbstractViewHolder> funcCreate) {
            this.layout = layout;
            this.funcCreate = funcCreate;
            id = IdHolder.id++;
        }

        public static ListItemType findById(int id) {
            for (ListItemType m : values()) {
                if (m.id == id) {
                    return m;
                }
            }
            return null;
        }

        private static class IdHolder {
            public static int id;
        }
    }

    public static class ListItem {
        public ListItemType type;
        User user;

        public ListItem() {

        }

        public ListItem(ListItemType type) {
            this.type = type;
        }

        public static ListItem createUser(User user) {
            ListItem listItem = new ListItem();
            listItem.type = ListItemType.USER;
            listItem.user = user;
            return listItem;
        }
    }
}
