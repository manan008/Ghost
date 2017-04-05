package com.google.engedu.ghost3;

import android.util.Log;

import java.util.HashMap;


public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        TrieNode temp=this;
        for(int i=0;i<s.length();i++)
        {
            char c=s.charAt(i);
            if(temp.children.containsKey(c))
            {
                    temp=temp.children.get(c);
            }
            else
                {
                    temp.children.put(c,new TrieNode());
                    temp=temp.children.get(c);
                }
        }
        temp.isWord=true;
        //Log.i("App1","Word:"+s+" Added"+"value isWord"+temp.isWord);
    }

    public boolean isWord(String s) {
        TrieNode temp = this;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (temp.children.containsKey(c)) {
                temp = temp.children.get(c);
            } else return false;
        }
        if (temp.isWord) {
            Log.i("App1", "Word Found");
            return true;
        } else {
            Log.i("App1", "Word Not Found");
            return false;
        }
    }

    private TrieNode searchNode(String prefix)
    {
        Log.i("App1","Starting search function");
        TrieNode temp=null;
        HashMap<Character,TrieNode> child=children;

        for(int i=0;i<prefix.length();i++)
        {
            Character c=prefix.charAt(i);
            if(child.containsKey(c))
            {
                Log.i("App1","search function at char:"+c);
                temp=child.get(c);
                child=temp.children;
            }
            else return null;
        }
        return temp;
    }

    public String getAnyWordStartingWith(String s)
    {
        Log.i("App1","good wordstarting with starts");
        TrieNode trie=searchNode(s);
        Log.i("App1",""+trie.toString());
        String word=s+"";
        if(trie==null)return null;
        else
        {
            while(!trie.isWord)
            {
                Object c[]=trie.children.keySet().toArray();
                int size=c.length;
                Log.i("App1","Size of char aray:"+size);
                int randindex=(int)(Math.random()*size);
                Log.i("App1","Random index of char aray:"+randindex);
                char c1=(char)c[randindex];
                Log.i("App1","CharACTER ADDED TO WORD:"+c1);
                word+=c1;
                trie=trie.children.get(c1);
            }
        }
        Log.i("App1","FINAL WORD:"+word);
        return word;
    }

    public String getGoodWordStartingWith(String s) {
        if(s.equals(""))
        {
            Object c[]= children.keySet().toArray();
            int size=c.length;
            Log.i("App1","Size of char aray:"+size);
            int randindex=(int)(Math.random()*size);
            Log.i("App1","Random index of char aray:"+randindex);
            String word =""+(char)c[randindex];
            Log.i("App1","New word of char array:"+word);
            return word;
        }

        TrieNode trie=searchNode(s);
        String word=s+"";
        if(trie==null)return null;
        else
        {
            Object c[]=trie.children.keySet().toArray();
            int size=c.length;
            Log.i("App1","Size of char aray:"+size);
            int randindex=(int)(Math.random()*size);
            Log.i("App1","Random index of char aray:"+randindex);
            char letter=(char)c[randindex];
            Log.i("App1","checking isword variable for word: "+word+" and added letter "+letter+"  isWord:"+trie.children.get(letter).isWord);
            //To check if all new possible words are valid word or not

            int sizecheck=0;
            while(trie.children.get(letter).isWord&&sizecheck<size)  //generating randomindex.iterating by one after that.check validity for each possible letter in trie
            {
                Log.i("App1","Inside Loop");
                sizecheck++;
                randindex=(++randindex%size);
                letter=(char)c[randindex];
                Log.i("App1","checking isword for word:"+word+letter);
            }
            if(sizecheck>=size)                             //forms valid word after adding every possible letter
            {
                int randindex2=(int)(Math.random()*size);
                letter=(char)c[randindex2];
            }
            word+=letter;
            Log.i("App1","New word of char array:"+word);
        }

        return word;
    }
}
