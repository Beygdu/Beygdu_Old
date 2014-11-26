package is.arnastofnun.fragments;

import is.arnastofnun.parser.Block;
import is.arnastofnun.parser.SubBlock;
import is.arnastofnun.parser.Tables;
import is.arnastofnun.parser.WordResult;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.beygdu.R;

/**
	 * @author Jón Friðrik Jónatansson
	 * @since 20.10.14
	 * @version 0.2
	 * A placeholder fragment containing a simple view.
	 */
	public class TableFragment extends Fragment {

		/**
		 * content er arraylisti af þeim strengjum, í réttri röð, sem eiga að birtast
		 * description - er nafn töflunar.
		 */
		private Context context;
		private TableLayout tableLayout;
		private Block block;
		private TextView title;
		
				
		/**
		 * @param context the context in which the fragment will be placed
		 * @param tableLayout
		 * @param block
		 * @param title 
		 */
		public TableFragment(Context context, TableLayout tableLayout, Block block, TextView title) {
			this.context = context;
			this.tableLayout = tableLayout;
			this.block = block;
			this.title = title;
		}
		
		public CharSequence getTitle() {
			return title.getText();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.table,
					container, false);
			createBlock();
			return rootView;
		}
		
		private void createBlock() {
			tableLayout.addView(title);
			//Iterate through sub-blocks and set title
			for (SubBlock sBlock: block.getBlocks()){
				if(!sBlock.getTitle().equals("")) {
					TextView subBlockTitle = new TextView(context);
					subBlockTitle.setText(sBlock.getTitle());
					subBlockTitle.setTextSize(25);
					subBlockTitle.setHeight(50);
					tableLayout.addView(subBlockTitle);
				}
				//Create the tables and set title
				for (Tables tables : sBlock.getTables()) {
//					if(!tables.getTitle().equals("")) {
						TextView tableTitle = new TextView(context);
						tableTitle.setText(tables.getTitle());
						tableTitle.setTextSize(20);
						tableTitle.setHeight(50);
						tableLayout.addView(tableTitle);
						createTable(tables);
				}
			}				
		}	
		
		private void createTable(Tables tables) {
			final int rowNum = tables.getRowNames().length;
			final int colNum = tables.getColumnNames().length;
			
			TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
			tableRowParams.setMargins(1, 1, 1, 1);
			tableRowParams.weight = colNum;
//			tableRowParams.height = 100;
			
			int contentIndex = 0;
			for (int row = 0; row < rowNum; row++) {
				TableRow tr = new TableRow(context);
				
				
				tr.setLayoutParams(tableRowParams);
				
				tr.setBackgroundColor(getResources().getColor(R.color.grey));
				for (int col = 0; col < colNum; col++) {
					TextView cell = new TextView(context);
					cell.setTextAppearance(context, R.style.BodyText);
					cell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
					cell.setGravity(Gravity.CENTER);
					cell.setTextColor(getResources().getColor(R.color.navy));
					cell.setBackgroundResource(R.drawable.border);
					if (row == 0) {
						if (tables.getContent().size() == 1) {
							cell.setText(tables.getContent().get(row));
						} else {
							cell.setText(tables.getColumnNames()[col]);
						}
					} else {
						if (col == 0) {
							cell.setText(tables.getRowNames()[row]);
						} else {
							cell.setText(tables.getContent().get(contentIndex++));
						}
					}
					tr.addView(cell);
				}
				tableLayout.addView(tr);
			}
			
		}
	}