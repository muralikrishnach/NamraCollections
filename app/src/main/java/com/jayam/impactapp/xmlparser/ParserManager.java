/**
 * This class will define that, which type of parsing is to be held.
 */
package com.jayam.impactapp.xmlparser;

public class ParserManager 
{
	public enum ParserType 
	{
		SAX, DOM, ANDROID_SAX, ANDROID_PARSER, ANDROID_SAX1;
	}
	public static XMLParser getParser(ParserType type) 
	{
		switch (type) 
		{
			case SAX:
				// return new SaxFeedParserlogin(feedUrl);
			case ANDROID_SAX:			
				return new XMLParser();
			default:
				return null;
		}
	}
}
