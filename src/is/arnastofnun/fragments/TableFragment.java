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
		private WordResult words;
		
				
		/**
		 * @param context the context in which the fragment will be placed
		 * @param tableLayout
		 * @param tables
		 */
		public TableFragment(Context context, TableLayout tableLayout, WordResult words) {
			this.context = context;
			this.tableLayout = tableLayout;
			this.words = words;
		}
		
		@SuppressWarnings("javadoc")
		public static TableFragment newInstance(Context context, TableLayout tableLayout, WordResult words) {
	        TableFragment f = new TableFragment(context, tableLayout, words);
	        return f;
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
			//Iterate through blocks and set title
			for (Block block : words.getBlocks()) {
				if(!block.getTitle().equals("")) {
					TextView blockTitle = new TextView(context);
					blockTitle.setText(block.getTitle());
					tableLayout.addView(blockTitle);
				}	
				//Iterate through sub-blocks and set title
				for (SubBlock sBlock: block.getBlocks()){
					if(!sBlock.getTitle().equals("")) {
						TextView subBlockTitle = new TextView(context);
						subBlockTitle.setText(sBlock.getTitle());
						tableLayout.addView(subBlockTitle);
					}
					//Create the tables and set title
					for (Tables tables : sBlock.getTables()) {
						if(!tables.getTitle().equals("")) {
							TextView tableTitle = new TextView(context);
							tableTitle.setText(tables.getTitle());
							tableLayout.addView(tableTitle);
						}
						createTable(tables);
					}
				}				
			}
		}	
		
		private void createTable(Tables tables) {
			int rowNum = tables.getRowNames().length;
			int colNum = tables.getColumnNames().length;
			
			TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(10);
			tableRowParams.setMargins(1, 1, 1, 1);
			tableRowParams.weight = 1;
			tableRowParams.height = 100;
			
			int contentIndex = 0;
			for (int row = 0; row < rowNum; row++) {
				TableRow tr = new TableRow(context);
				
				
				tr.setLayoutParams(tableRowParams);
				
				tr.setBackgroundColor(Color.BLACK);
				for (int col = 0; col < colNum; col++) {
					TextView cell = new TextView(context);
					cell.setTextAppearance(context, R.style.BodyText);
					cell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
					cell.setGravity(Gravity.CENTER);
					cell.setBackgroundColor(Color.LTGRAY);
					cell.setTextColor(Color.WHITE);
					if (row == 0) {
						cell.setText(tables.getColumnNames()[col]);
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