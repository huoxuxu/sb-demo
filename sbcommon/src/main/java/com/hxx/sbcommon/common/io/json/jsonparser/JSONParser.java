package com.hxx.sbcommon.common.io.json.jsonparser;

import com.hxx.sbcommon.common.io.json.jsonparser.parser.Parser;
import com.hxx.sbcommon.common.io.json.jsonparser.common.util.CharReaderUtil;
import com.hxx.sbcommon.common.io.json.jsonparser.tokenizer.TokenList;
import com.hxx.sbcommon.common.io.json.jsonparser.tokenizer.Tokenizer;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by code4wt on 17/9/1.
 * <a href="https://github.com/code4wt/JSONParser">版权归原作者所有，此处仅做为学习研究之用</a>
 */
public class JSONParser {

    private final Tokenizer tokenizer = new Tokenizer();

    private final Parser parser = new Parser();

    public Object fromJSON(String json) throws IOException {
        CharReaderUtil charReader = new CharReaderUtil(new StringReader(json));
        TokenList tokens = tokenizer.tokenize(charReader);
        return parser.parse(tokens);
    }
}
