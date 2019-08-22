/*
 * Copyright 2012 Thinkermobile, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.james.easyclass.imageloader;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.graphics.Bitmap;


public class MemoryCache {
    private Map<String, SoftReference<Bitmap>> cache = Collections.synchronizedMap(new HashMap<String, SoftReference<Bitmap>>());
    
    public Bitmap get(String id){
        if(!cache.containsKey(id))
            return null;
        SoftReference<Bitmap> ref = cache.get(id);
        if(ref != null && ref.get() != null){
			return ref.get();
		}
        return null;
    }
    
    public void put(String id, Bitmap bitmap){
        cache.put(id, new SoftReference<Bitmap>(bitmap));
    }

    public void clear() {
    	Set<Entry<String, SoftReference<Bitmap>>> set = cache.entrySet();
    	Iterator<Entry<String, SoftReference<Bitmap>>> iterator = set.iterator();
    	
    	while(iterator.hasNext()){
    		SoftReference<Bitmap> ref = iterator.next().getValue();
    		if(ref != null && ref.get() != null){
    			ref.get().recycle();
    		}
    	}
    	
        cache.clear();
    }
    
    public void remove(String id){
    	SoftReference<Bitmap> ref = cache.get(id);
        if(ref != null && ref.get() != null){
        	ref.get().recycle();
		}
    	cache.remove(id);
    }
}