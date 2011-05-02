    <!---
    Loop over all the cookies and expire them. In reality, we only
    need to expire the CFID and CFTOKEN session identifiers, but
    for our purposes, clearing all the cookies is sufficient.
    --->
    <cfloop
    item="name"
    collection="#cookie#">
     
    <!---
    Have them expire immediately. This way, when the
    response is sent to the browser, all of their cookies
    will be cleared.
    --->
    <cfcookie
    name="#name#"
    value=""
    expires="now"
    />
     
    </cfloop>
     <a href="index.cfm">CLEARED</a>

    