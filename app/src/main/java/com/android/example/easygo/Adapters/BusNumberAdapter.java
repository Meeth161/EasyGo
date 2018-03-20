package com.android.example.easygo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.example.easygo.Activities.MapsActivity;
import com.android.example.easygo.R;
import com.android.example.easygo.Route;

import java.util.ArrayList;


public class BusNumberAdapter extends RecyclerView.Adapter<BusNumberAdapter.ViewHolder> {

    Context context;
    ArrayList<Route> routes;

    public BusNumberAdapter(Context context, ArrayList<Route> routes) {
        this.context = context;
        this.routes = routes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.bus_number_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Route r = routes.get(position);
        holder.tvBusNumber.setText(r.getRouteNumber());
        holder.tvSource.setText(r.getSource());
        holder.tvDestination.setText(r.getDestination());
        holder.llRouteNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MapsActivity.class).putExtra("rn", r.getRouteNumber()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llRouteNumber;
        TextView tvBusNumber;
        TextView tvSource;
        TextView tvDestination;

        public ViewHolder(View itemView) {
            super(itemView);

            llRouteNumber = (LinearLayout) itemView.findViewById(R.id.ll_routeNumber);
            tvBusNumber = (TextView) itemView.findViewById(R.id.textView_busNumber);
            tvSource = (TextView) itemView.findViewById(R.id.textView_source);
            tvDestination = (TextView) itemView.findViewById(R.id.textView_destination);
        }
    }

}
