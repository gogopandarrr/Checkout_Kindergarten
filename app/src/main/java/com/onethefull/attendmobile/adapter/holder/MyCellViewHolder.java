package com.onethefull.attendmobile.adapter.holder;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.model.Cell;

public class MyCellViewHolder extends AbstractViewHolder {

    public final TextView tv_cell;
    public LinearLayout container_cell;
    private Cell cell;

    public MyCellViewHolder(View itemView) {
        super(itemView);
        container_cell = itemView.findViewById(R.id.cell_container);
        tv_cell = itemView.findViewById(R.id.cell_data);

    }

    public void setCell(Cell cell){

        this.cell = cell;

        tv_cell.setText(String.valueOf((cell.getData())));
        container_cell.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        tv_cell.requestLayout();




    }

    @Override
    public void setSelected(SelectionState selectionState) {
        super.setSelected(selectionState);

        setBackgroundColor(cell.getBackgroundColor());
    }
}
