package com.onethefull.attendmobile.adapter.popup;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.sort.SortState;
import com.onethefull.attendmobile.adapter.holder.MyColumnHeaderViewHolder;

public class ColumnHeaderLongPressPopup extends PopupMenu implements PopupMenu.OnMenuItemClickListener {
    private static final String TAG = ColumnHeaderLongPressPopup.class.getSimpleName();

    // Sort states
    private static final int ASCENDING = 1;
    private static final int DESCENDING = 2;
    private static final int CLEAR = 3;
    // Test menu items for showing / hiding row
    private static final int ROW_HIDE = 4;
    private static final int ROW_SHOW = 3;

    private static final int TEST_ROW_INDEX = 4;

    private MyColumnHeaderViewHolder columnHeaderViewHolder;
    private ITableView iTableView;
    private Context context;
    private int position;


    public ColumnHeaderLongPressPopup (MyColumnHeaderViewHolder columnHeaderViewHolder, ITableView iTableView){
        super(columnHeaderViewHolder.itemView.getContext(), columnHeaderViewHolder.itemView);
        this.columnHeaderViewHolder = columnHeaderViewHolder;
        this.iTableView = iTableView;
        this.context = columnHeaderViewHolder.itemView.getContext();
        this.position = columnHeaderViewHolder.getAdapterPosition();

        columnHeaderViewHolder = (MyColumnHeaderViewHolder) iTableView.getColumnHeaderRecyclerView().
                findViewHolderForAdapterPosition(position);


        initialize();

    }//

    private void initialize(){
        createMenuItem();
        changeMenuItemVisibility();

        this.setOnMenuItemClickListener(this);

    }//

    private void createMenuItem(){
        this.getMenu().add(Menu.NONE, ASCENDING, 0, "오름차순 정렬");
        this.getMenu().add(Menu.NONE, DESCENDING, 1, "내림차순 정렬");


    }//

    private void changeMenuItemVisibility(){

        SortState sortState = iTableView.getSortingStatus(position);
        if (sortState == SortState.UNSORTED){

        } else if (sortState == sortState.DESCENDING){
            getMenu().getItem(1).setVisible(false);
        } else if (sortState == sortState.ASCENDING){
            getMenu().getItem(0).setVisible(false);
        }




    }//



    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case ASCENDING:
                iTableView.sortColumn(position, SortState.ASCENDING);
                break;


            case DESCENDING:
                iTableView.sortColumn(position, SortState.DESCENDING);
                break;

        }

        iTableView.remeasureColumnWidth(position);
        return true;
    }
}//
