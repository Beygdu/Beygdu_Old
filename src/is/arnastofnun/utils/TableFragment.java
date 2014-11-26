package is.arnastofnun.utils;

import is.arnastofnun.parser.Block;
import is.arnastofnun.parser.SubBlock;
import is.arnastofnun.parser.Tables;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.beygdu.R;

/**
	 * @author Jón Friðrik Jónatansson
	 * @since 20.10.14
	 * @version 0.9
	 * 
	 * Generísk tafla sem er útbúin út frá WordResult hlutnum.
	 * Fjöldi raða og dálka veldur á fjöld af dálka og raðar hausum í WordResult hlutnum. 
	 */
	public class TableFragment extends Fragment {

		/**
		 * context - er contextið sem taflan birtist í
		 * content er arraylisti af þeim strengjum, í réttri röð, sem eiga að birtast
		 * block - inniheldur raðar og column headerana og contentið á töflunni
		 * title - er titilinn á töflunni
		 */
		private Context context;
		private TableLayout tableLayout;
		private Block block;
		private TextView title;
		
				
		/**
		 * @param context er contextið sem taflan mun birtast í.
		 * @param tableLayout - er layoutið sem taflan er sett í.
		 * @param block - inniheldur raðar og column headerana og contentið á töflunni
		 * @param title - er titilinn á töflunni
		 */
		public TableFragment(Context context, TableLayout tableLayout, Block block, TextView title) {
			this.context = context;
			this.tableLayout = tableLayout;
			this.block = block;
			this.title = title;
		}
		
		/**
		 * @return titilinn á töflunni.
		 */
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
		
		/**
		 * býr til textview með titli subblokkar ef hann er til.
		 * Býr síðan til TextView með titil á töflunni og kallar síðan á 
		 * createTable sem smíðar töfluna fyrir allar töflur í subblokkinu.
		 */
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
		
		/**
		 * @param table taflan sem á að smíða
		 * Býr til TableRow fyrir hverja röð og TextView fyrir hvern column.
		 */
		private void createTable(Tables table) {
			final int rowNum = table.getRowNames().length;
			final int colNum = table.getColumnNames().length;
			
			TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
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
					cell.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
					cell.setGravity(Gravity.CENTER);
					cell.setTextColor(getResources().getColor(R.color.navy));
					cell.setBackgroundResource(R.drawable.border);
					if (row == 0) {
						if (table.getContent().size() == 1) {
							cell.setText(table.getContent().get(row));
						} else {
							cell.setText(table.getColumnNames()[col]);
						}
					} else {
						if (col == 0) {
							cell.setText(table.getRowNames()[row]);
						} else {
							cell.setText(table.getContent().get(contentIndex++));
						}
					}
					tr.addView(cell);
				}
				tableLayout.addView(tr);
			}
		}
	}