package com.google.engedu.ghost3;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<String>();
        String line=null;
        while((line = in.readLine()) != null){
            if (line.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
        Log.i("App1","Size"+words.size());
    }

    @Override
    public boolean isWord(String word) {
        return  words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix)
    {
        if(prefix.equals(""))
        {
           int index= (int)(Math.random()*words.size());
            Log.i("App1","random index"+index);
            return words.get(index);
        }
        int index=binsearch(prefix);
        Log.i("App1","Bin srch"+index);
        if(index==-1)return null;
        else return words.get(index);
    }


    @Override
    public String getGoodWordStartingWith(String prefix) {
        //String selected = null;
        if(prefix.equals(""))
        {
            int index= (int)(Math.random()*words.size());
            Log.i("App1","random index"+index);
            return words.get(index);
        }
        int first_index =binsearch(prefix);
        if(first_index==-1)return null;
        int low_bound=lowBound(prefix,0,first_index);
        int up_bound=upBound(prefix,first_index,words.size()-1);

        Log.i("App1","Word:"+prefix);
        Log.i("App1","Low Bound:"+words.get(low_bound));
        Log.i("App1","Up Bound:"+words.get(up_bound));

        ArrayList<String> evenlengthwords=new ArrayList<>();
        ArrayList<String> oddlengthwords=new ArrayList<>();
        for(int i=low_bound;i<=up_bound;i++)
        {
            String word=words.get(i);
            if(word.length()%2==0)evenlengthwords.add(word);
            else oddlengthwords.add(word);
        }
        if(prefix.length()%2==0||oddlengthwords.isEmpty()&&!evenlengthwords.isEmpty())
        {
            return evenlengthwords.get((int)Math.random()*evenlengthwords.size());
        }
        else
          return oddlengthwords.get((int)(Math.random()*(oddlengthwords.size())));

    //    return selected;
    }

    private int lowBound(String prefix,int low,int high)
    {
        if(low>high)
            return low;
        int mid=low+((high-low)>>1);

        if(words.get(mid).compareTo(prefix)>=0)
        {
            Log.i("App1","No word found at:"+mid+"Word:"+words.get(mid));
            return lowBound(prefix,low,mid-1);
        }
        else
        {
            Log.i("App1","No word found at:"+mid+"Word:"+words.get(mid));
            return lowBound(prefix,mid+1,high);
        }
    }

    private int upBound(String prefix,int low,int high)
    {

        if(low>=high)
            return low;
        int mid=low+((high-low)>>1);
        Log.i("App1","UpBound:low:"+low+"  mid"+mid+"  high:"+high);
        boolean b=!words.get(mid).substring(0,Math.min(words.get(mid).length(),prefix.length())).equals(prefix);
        if(words.get(mid).compareTo(prefix)>0 && b)
        {
            Log.i("App1","Compare to result:"+words.get(mid).compareTo(prefix));
            Log.i("App1","No word found at:"+mid+"Word:"+words.get(mid));
            return upBound(prefix,low,mid-1);
        }
        else
        {
            Log.i("App1","Compare to <0 result:"+words.get(mid).compareTo(prefix));
            Log.i("App1","No word found at:"+mid+"Word:"+words.get(mid));
            return upBound(prefix,mid+1,high);
        }
    }


    private int binsearch(String prefix)
    {
        int index=-1;
        int low=0;
        int high=words.size()-1;
        while(low<=high)
        {
            int mid=(low+high)/2;
            if(words.get(mid).startsWith(prefix)) {Log.i("App1","Found Word:"+words.get(mid));index=mid;break;}
            else
            {
                if(words.get(mid).compareTo(prefix)<0)
                {
                    Log.i("App1","No word found at:"+mid+"Word:"+words.get(mid));
                    low=mid+1;
                }
                else {
                    Log.i("App1","No word found at:"+mid+"Word:"+words.get(mid));
                    high = mid - 1;
                }
            }
        }
        return index;
    }
}
