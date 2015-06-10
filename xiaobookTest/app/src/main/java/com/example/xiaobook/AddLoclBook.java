// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import java.io.File;
import java.util.ArrayList;

// Referenced classes of package com.example.xiaobook:
//            BookShelfActivity

public class AddLoclBook extends ListActivity
{
    public class MyAdapter extends BaseAdapter
    {

        public int getCount()
        {
            return AddLoclBook.mAllTxtFile.size();
        }

        public Object getItem(int i)
        {
            return null;
        }

        public long getItemId(int i)
        {
            return 0L;
        }

        public View getView(int i, View view, ViewGroup viewgroup)
        {
            ViewHolder viewholder;
            final int j = i;
            if(view == null)
            {
                viewholder = new ViewHolder();
                view = mInflater.inflate(R.layout.add_list_item, null);
                viewholder.book_name = (TextView)view.findViewById(R.id.new_local_book_name);
                viewholder.add_to_shelf = (CheckBox)view.findViewById(R.id.add_book_checkbox);
                view.setTag(viewholder);
            } else
            {
                viewholder = (ViewHolder)view.getTag();
            }

            viewholder.book_name.setText((mAllTxtFile.get(i)).getName());
            viewholder.add_to_shelf.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

                public void onCheckedChanged(CompoundButton compoundbutton, boolean flag)
                {
                if(flag)
                {
                    chosenBook.add(j);
                    return;
                } else
                {
                    chosenBook.remove(AddLoclBook.chosenBook.indexOf(j));
                    return;
                }
                }


            });
            return view;
        }

        private LayoutInflater mInflater;
        final AddLoclBook this$0;

        public MyAdapter(Context context)
        {
            this$0 = AddLoclBook.this;
            mInflater = LayoutInflater.from(context);
        }
    }

    final class ViewHolder
    {

        public CheckBox add_to_shelf;
        public TextView book_name;

    }


    public AddLoclBook()
    {
        TAG = "AddLoclBook";
    }

    private static ArrayList findFile(File file, String s)
    {
        ArrayList arraylist = new ArrayList();
        if(file.isDirectory())
        {
            File afile[] = file.listFiles();
            if(afile != null)
            {
                int i = afile.length;
                int j = 0;
                while(j < i) 
                {
                    File file1 = afile[j];
                    if(file1.isDirectory())
                    {
                        if(file1.getName().toLowerCase().lastIndexOf(s) > -1)
                            arraylist.add(file1);
                        arraylist.addAll(findFile(file1, s));
                    } else
                    if(file1.getName().toLowerCase().lastIndexOf(s) > -1)
                        arraylist.add(file1);
                    j++;
                }
            }
        }
        return arraylist;
    }

    private void removeBookOnShelf(ArrayList arraylist, ArrayList arraylist1)
    {
        int i = arraylist.size();
        Log.i(TAG, (new StringBuilder()).append("aaaaaaaaaaaaaaaaaa").append(i).toString());
        for(int j = 0; j < i; j++)
        {
            Log.i(TAG, (new StringBuilder()).append("titles.size()").append(arraylist1.size()).toString());
            if(j > -1 + arraylist1.size())
                continue;
            for(int k = 0; k <= -1 + arraylist1.size(); k++)
            {
                Log.i(TAG, (new StringBuilder()).append("ready remove titles:").append(k).append(":").append((String)arraylist1.get(k)).toString());
                Log.i(TAG, (new StringBuilder()).append("ready remove files:").append(j).append(":").append(((File)arraylist.get(j)).getName().substring(0, -4 + ((File)arraylist.get(j)).getName().length())).toString());
                if(((String)arraylist1.get(k)).equals(((File)arraylist.get(j)).getName().substring(0, -4 + ((File)arraylist.get(j)).getName().length())))
                {
                    Log.i(TAG, (new StringBuilder()).append("remove titles:").append(k).append(":").append((String)arraylist1.get(k)).toString());
                    Log.i(TAG, (new StringBuilder()).append("remove files:").append(j).append(":").append(((File)arraylist.get(j)).getName().substring(0, ((File)arraylist.get(j)).getName().length())).toString());
                    arraylist.remove(j);
                }
            }

        }

    }

    private void removeSameBook(ArrayList arraylist)
    {
        int i = arraylist.size();
        for(int j = 0; j < i; j++)
        {
            if(j >= arraylist.size())
                continue;
            for(int k = j + 1; k < arraylist.size(); k++)
                if(((File)arraylist.get(j)).getName().equals(((File)arraylist.get(k)).getName()))
                    arraylist.remove(k);

        }

    }

    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.all_local_books);
        chosenBook.clear();
        File file = new File("/mnt");
        mAllTxtFile.clear();
        mAllTxtFile = findFile(file, ".txt");
        removeSameBook(mAllTxtFile);
        removeBookOnShelf(mAllTxtFile, BookShelfActivity.mTitles);
        setListAdapter(new MyAdapter(this));
        AddBookToShelf = (Button)findViewById(R.id.add_to_shelf);
        AddBookToShelf.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view) {
                for (int i = 0; i < chosenBook.size(); i++) {
                    Log.i("TTTTTT", "" + mAllTxtFile.size());
                    Log.i("SSSSSS", "" + chosenBook.size());
                    Log.i("chosenBook.get(i)", "" + chosenBook.get(i));
                    BookShelfActivity.mPictures.add(BookShelfActivity.bookCover);
                    BookShelfActivity.mTitles.add((mAllTxtFile.get(chosenBook.get(i)))
                            .getName().substring(0, mAllTxtFile.get((chosenBook.get(i))).getName().length() - 4));
                    //BookShelfActivity.bookslistid.add((mAllTxtFile.get((chosenBook.get(i)))).hashCode());
                    BookShelfActivity.bookslistid.add(hashCode());
                }
                BookShelfActivity.mBookShelfActivity.finish();

                Intent intent = new Intent();
                intent.setClassName("com.example.xiaobook", "com.example.xiaobook.BookShelfActivity");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

            final AddLoclBook this$0;


            {
                this$0 = AddLoclBook.this;

            }
        });
    }


    static ArrayList<Integer> chosenBook = new ArrayList<Integer>();
    static ArrayList<File> mAllTxtFile = new ArrayList<File>();
    private Button AddBookToShelf;
    private String TAG;


}
