package com.onethefull.attendmobile.adapter.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.model.RowHeader;

public class MyRowHeaderViewHolder extends AbstractViewHolder {

    public final TextView tv_row_header;
    LinearLayout container_row_header;
    RowHeader rowHeader;
    public MyRowHeaderViewHolder(View itemView) {
        super(itemView);
        tv_row_header = itemView.findViewById(R.id.tv_row_header);
        container_row_header = itemView.findViewById(R.id.container_row_header);

    }

    public void setRowHeader(RowHeader rowHeader){

        this.rowHeader = rowHeader;

        tv_row_header.setText(rowHeader.getData());

    }

    @Override
    public void setSelected(SelectionState selectionState) {
        super.setSelected(selectionState);

        setBackgroundColor(rowHeader.getBackgroundColor());

    }
}
