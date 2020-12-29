package com.mykids.mybarron;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.*;

public class MyActivity extends Activity {

    TextView word;
    Button next;
    Button previous;
    Button random;
    Spinner page;
    List<Vocab> vocabs;
    int wordPointer;
    Integer[] pagesArray;
    boolean showingWord;
    Button load;
    Button loadAll;
    EditText pagesInput;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        word = (TextView) this.findViewById(R.id.word);
        next = (Button) this.findViewById(R.id.next);
        previous = (Button) this.findViewById(R.id.previous);
        random = (Button) this.findViewById(R.id.random);
        page = (Spinner) this.findViewById(R.id.page);
        load = (Button) this.findViewById(R.id.load);
        loadAll = (Button) this.findViewById(R.id.loadAll);
        pagesInput = (EditText) this.findViewById(R.id.pagesInput);

        vocabs = new ArrayList<Vocab>();
        vocabs = getVocabDBHelper().getVocabInPage(1);
        
        pagesArray  = getVocabDBHelper().getDistictPageNoArray();
        ArrayAdapter<Integer> pageAdaptor = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, pagesArray);
        page.setAdapter(pageAdaptor);

        showWord();
        
        setListener();
    }

    public VocabDBHelper getVocabDBHelper(){
        return new VocabDBHelper(this);
    }
    
    public boolean isPointerInVocabRange(){
		if(wordPointer >= vocabs.size()){
			return false;
    	}
		return true;
    }
    
    public void showWord(){
    	if(isPointerInVocabRange()){
    		word.setText(vocabs.get(wordPointer).getWord());
        	showingWord = true;	
    	}
    }
    
    public void showMeaning(){
    	if(isPointerInVocabRange()){
	    	word.setText(vocabs.get(wordPointer).getMeaning());
	    	showingWord = false;
    	}
    }

    public void setListener(){
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordPointer++;

                if(wordPointer > vocabs.size()){
                    wordPointer = 0;
                    
                    Toast.makeText(getApplicationContext(), "Reached the last, go backward!!",
    						Toast.LENGTH_SHORT).show();
                }
                showWord();
                
                checkIfVocabWentEmpty();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordPointer--;

                if(wordPointer < 0){
                    wordPointer = 0;
                    
                    Toast.makeText(getApplicationContext(), "Reached the top, go forward!!",
    						Toast.LENGTH_SHORT).show();
                }

                showWord();
                
                checkIfVocabWentEmpty();
            }
        });

        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               wordPointer = Utils.randInt(0, vocabs.size()-1);
               showWord();
            }
        });

        page.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                vocabs = getVocabDBHelper().getVocabInPage(pagesArray[position]);
                wordPointer = 0;
                showWord();
                
                checkIfVocabWentEmpty();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	if(showingWord){
            		showMeaning();
            	} else{
            		showWord();
            	}
              
            	checkIfVocabWentEmpty();
            }
        });
        
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Integer[] pages = new Integer[0];
            	try{
            		pages = Utils.getArrayFromCommaSeperatedString(pagesInput.getText().toString());	
            	
	            	if(isValidPageNumberProvided(pages)){
	            		vocabs = getVocabDBHelper().getVocabInPage(pages);
	                    wordPointer = 0;
	                    showWord();
	                    
	                    checkIfVocabWentEmpty();
	            	} else{
	            		Toast.makeText(getApplicationContext(), "Invalid page number provided, please type the correct page",
	            				Toast.LENGTH_SHORT).show();
	            	}
            	
            	} catch(UnsupportedParameterException e){
            		Toast.makeText(getApplicationContext(), "Wrong input!!",
    						Toast.LENGTH_SHORT).show();
            	}
            	
            	
            }
        });
        
        loadAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	vocabs = getVocabDBHelper().getVocabInPage();
                wordPointer = 0;
                showWord();
                
                checkIfVocabWentEmpty();
            }
        });
        
        
    }
    
    protected boolean isValidPageNumberProvided(Integer[] enteredPages) {
		for(int enteredPage: enteredPages){
			if(!Arrays.asList(pagesArray).contains(enteredPage)){
				return false;
			}
		}
		return true;
	}

	public void reportErrorToDev(String error){
    	Toast.makeText(getApplicationContext(), error,
				Toast.LENGTH_SHORT).show();
    }
    
    public void checkIfVocabWentEmpty(){
    	 if(vocabs.isEmpty()){
         	reportErrorToDev("Ooops!! vocabs went empty. Hold on fixing it");
         	vocabs = getVocabDBHelper().getVocabInPage(0);
         	wordPointer = 0;
         }
    }



}
