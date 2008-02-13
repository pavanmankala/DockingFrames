/*
 * Bibliothek - DockingFrames
 * Library built on Java/Swing, allows the user to "drag and drop"
 * panels containing any Swing-Component the developer likes to add.
 * 
 * Copyright (C) 2007 Benjamin Sigg
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * Benjamin Sigg
 * benjamin_sigg@gmx.ch
 * CH - Switzerland
 */
package bibliothek.util.xml;

/**
 * An attribute is an element in a XML-file that has a name and perhaps a value.
 * @author Benjamin Sigg
 */
public class XAttribute {
    /** the name of this attribute */
    private String name;
    /** the value of this attribute */
    private String value = "";

    /**
     * Creates a new attribute.
     * @param name the name of the attribute
     */
    public XAttribute( String name ){
        setName( name );
    }
    
    /**
     * Sets the name of this attribute.
     * @param name the new name
     */
    public void setName( String name ) {
        if( name == null )
            throw new IllegalArgumentException( "name must not be null" );
        
        this.name = name;
    }
    
    /**
     * Gets the name of this attribute.
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the exact value that is stored in this attribute.
     * @param value the value that will not be encoded by this method
     */
    public void setValue( String value ) {
        this.value = value;
    }
    
    /**
     * Gets the exact value that is stored in this attribute.
     * @return the value that is not encoded
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Sets the value of this attribute.
     * @param b the value
     */
    public void setByte( byte b ){
        value = String.valueOf( b );
    }
    
    /**
     * Sets the value of this attribute.
     * @param s the value
     */
    public void setShort( short s ){
        value = String.valueOf( s );
    }
    
    /**
     * Sets the value of this attribute.
     * @param i the value
     */
    public void setInt( int i ){
        value = String.valueOf( i );
    }
    
    /**
     * Sets the value of this attribute.
     * @param l the value
     */
    public void setLong( long l ){
        value = String.valueOf( l );
    }
    
    /**
     * Sets the value of this attribute.
     * @param b the value
     */
    public void setBoolean( boolean b ){
        value = String.valueOf( b );
    }
    
    /**
     * Sets the value of this attribute.
     * @param c the value
     */
    public void setChar( char c ){
        value = String.valueOf( c );
    }
    
    /**
     * Sets the value of this attribute.
     * @param s the value
     */
    public void setString( String s ){
        if( s == null )
            throw new IllegalArgumentException( "value must not be null" );
        value = s;
    }
    
    /**
     * Sets the value of this attribute.
     * @param f the value
     */
    public void setFloat( float f ){
        value = String.valueOf( f );
    }
    
    /**
     * Sets the value of this attribute.
     * @param d the value
     */
    public void setDouble( double d ){
        value = String.valueOf( d );
    }
    
    /**
     * Sets the value of this attribute.
     * @param bs the value
     */
    public void setByteArray( byte[] bs ){
        StringBuilder builder = new StringBuilder( bs.length*2 );
        for( byte b : bs ){
            builder.append( Integer.toHexString( (b >> 4) & 15 ) );
            builder.append( Integer.toHexString( b & 15 ));
        }
        value = builder.toString();
    }
    
    
    
    /**
     * Gets the value of this attribute as byte.
     * @return the value
     * @throws XException if value is not a byte
     */
    public byte getByte(){
        try{
            return Byte.parseByte( value );
        }
        catch( NumberFormatException ex ){
            throw new XException( ex );
        }
    }
    
    /**
     * Gets the value of this attribute as short.
     * @return the value
     * @throws XException if value is not a short
     */
    public short getShort(){
        try{
            return Short.parseShort( value );
        }
        catch( NumberFormatException ex ){
            throw new XException( ex );
        }
    }
    
    /**
     * Gets the value of this attribute as integer.
     * @return the value
     * @throws XException if value is not an integer
     */
    public int getInt(){
        try{
            return Integer.parseInt( value );
        }
        catch( NumberFormatException ex ){
            throw new XException( ex );
        }
    }
    
    /**
     * Gets the value of this attribute as long.
     * @return the value
     * @throws XException if value is not a long
     */
    public long getLong(){
        try{
            return Long.parseLong( value );
        }
        catch( NumberFormatException ex ){
            throw new XException( ex );
        }
    }
    
    /**
     * Gets the value of this attribute as float.
     * @return the value
     * @throws XException if value is not a float
     */
    public float getFloat(){
        try{
            return Float.parseFloat( value );
        }
        catch( NumberFormatException ex ){
            throw new XException( ex );
        }
    }
    
    /**
     * Gets the value of this attribute as double.
     * @return the value
     * @throws XException if value is not a double
     */
    public double getDouble(){
        try{
            return Double.parseDouble( value );
        }
        catch( NumberFormatException ex ){
            throw new XException( ex );
        }
    }
    
    /**
     * Gets the value of this attribute as character.
     * @return the value
     * @throws XException if value is not a character
     */
    public char getChar(){
        if( value.length() != 1 )
            throw new XException( "not a character: " + value );
        
        return value.charAt( 0 );
    }
    
    /**
     * Gets the value of this attribute as string.
     * @return the value
     * @throws XException if value is not a string
     */
    public String getString(){
        return value;
    }
    
    /**
     * Gets the value of this attribute as boolean.
     * @return the value
     * @throws XException if value is not a boolean
     */
    public boolean getBoolean(){
        String value = this.value.trim().toLowerCase();
        
        if( "true".equals( value ))
            return true;
        if( "on".equals( value ))
            return true;
        if( "1".equals( value ))
            return true;
        if( "yes".equals( value ))
            return true;
        
        if( "false".equals( value ))
            return false;
        if( "off".equals( value ))
            return false;
        if( "0".equals( value ))
            return false;
        if( "no".equals( value ))
            return false;
        
        throw new XException( "not a boolean: " + value );
    }
    
    /**
     * Gets the value of this attribute as byte-array.
     * @return the value
     * @throws XException if value is not a byte-array
     */
    public byte[] getByteArray(){
        if( value.length() % 2 != 0 )
            throw new XException( "value is not a byte-array: " + value );
        
        byte[] result = new byte[ value.length()/2 ];
        for( int i = 0, n = value.length(); i<n; i++ ){
            char c = value.charAt( i );
            
            int value = 0;
            switch( c ){
                case '0' : value = 0; break;
                case '1' : value = 1; break;
                case '2' : value = 2; break;
                case '3' : value = 3; break;
                case '4' : value = 4; break;
                case '5' : value = 5; break;
                case '6' : value = 6; break;
                case '7' : value = 7; break;
                case '8' : value = 8; break;
                case '9' : value = 9; break;
                case 'a' : value = 10; break;
                case 'b' : value = 11; break;
                case 'c' : value = 12; break;
                case 'd' : value = 13; break;
                case 'e' : value = 14; break;
                case 'f' : value = 15; break;
                default: throw new XException( "value is not a byte-array: " + value );
            }
            
            if( i % 2 == 0 ){
                result[i/2] = (byte)(value << 4);
            }
            else{
                result[i/2] |= value;
            }
        }
        
        return result;
    }
}