#!/usr/bin/python
from pydxml import IDXMLParser

xml = """
<extras>
<extra cost=\"3.00\" name=\"Bike\"/>
<extra cost=\"40.00\" name=\"Soiling\"/>
<extra cost=\"88.00\" name=\"Woman\"/>
<extra cost=\"135.00\" name=\"money\"/>
<extra cost=\"23827498.00\" name=\"ing\"/>
</extras>
"""

parser = IDXMLParser(xml)
print parser.getcount("extras:extra")
