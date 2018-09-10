package com.onethefull.attendmobile.adapter.holder;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.evrencoskun.tableview.sort.SortState;
import com.onethefull.attendmobile.R;

public class MyColumnHeaderViewHolder extends AbstractSorterViewHolder {

    public final TextView tv_column_header;
    LinearLayout container_column_header;
    ITableView iTableView;
    ImageButton btn_column_sort;

    public MyColumnHeaderViewHolder(View itemView, ITableView pTableView) {
        super(itemView);
        iTableView = pTableView;
        tv_column_header = itemView.findViewById(R.id.column_header_tv);
        container_column_header = itemView.findViewById(R.id.column_header_container);
        btn_column_sort = itemView.findViewById(R.id.btn_sort_column_header);

        btn_column_sort.setOnClickListener(sortButtonListener);
    }


    @Override
    public void setSelected(SelectionState selectionState) {
        super.setSelected(selectionState);

        int bgColorId, fgColorId;

        if (selectionState == selectionState.SELECTED){
            bgColorId = R.color.attend_bg1;
            fgColorId = R.color.selected_text_color;
        } else if (selectionState == selectionState.UNSELECTED){
            bgColorId = R.color.attend_bg1;
            fgColorId = R.color.unselected_text_color;
        } else {
            bgColorId = R.color.attend_bg1;
            fgColorId = R.color.unselected_text_color;
        }

        container_column_header.setBackgroundColor(ContextCompat.getColor(container_column_header.getContext(), bgColorId));
//        tv_column_header.setBackgroundColor(ContextCompat.getColor(container_column_header.getContext(), fgColorId));

    }

    @Override
    public void onSortingStatusChanged(SortState pSortState) {
        super.onSortingStatusChanged(pSortState);

        container_column_header.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        controlSortState(pSortState);

        tv_column_header.requestLayout();
        btn_column_sort.requestLayout();
        container_column_header.requestLayout();
        itemView.requestLayout();


    }

    private void controlSortState(SortState sortState){

        if (sortState == sortState.ASCENDING){
            btn_column_sort.setVisibility(View.VISIBLE);
            btn_column_sort.setImageResource(R.drawable.sort_down);
        }else if (sortState == sortState.DESCENDING){
            btn_column_sort.setVisibility(View.VISIBLE);
            btn_column_sort.setImageResource(R.drawable.sort_up);
        }else {
            btn_column_sort.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener sortButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (getSortState() == SortState.ASCENDING){
                iTableView.sortColumn(getAdapterPosition(), SortState.DESCENDING);
            } else if (getSortState() == SortState.DESCENDING){
                iTableView.sortColumn(getAdapterPosition(), SortState.ASCENDING);
            } else {
                iTableView.sortColumn(getAdapterPosition(), SortState.DESCENDING);
            }

        }
    };


}
