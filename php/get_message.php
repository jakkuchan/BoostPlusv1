<?php
   $json = file_get_contents('php://input');
   $obj = json_decode($json);
   
   $usr = "a9334981_admin";
   $pwd = "hope123";

   $db = "a9334981_hope";

   $host = "mysql8.000webhost.com";

   $cid = mysql_connect($host,$usr,$pwd);

   if(!$cid) { 
      echo("ERROR: " . mysql_error() . "\n");    
   }
 
   $sentiment = $obj->{"sentiment"};
  
   $query_max = "SELECT MAX(hope_id) AS max FROM messages_" . $sentiment;

   $max_result = mysql_db_query($db, $query_max, $cid) or die('Errant query:  ' . $query_max);
   
   $max_index = mysql_fetch_array($max_result);
   
   $rand_index = rand(1, $max_index['max']);

   $query_messages = "SELECT message, author FROM messages_" . $sentiment . " WHERE hope_id=" . $rand_index;
   $result = mysql_db_query($db, $query_messages, $cid) or die('Errant query: ' . $query_messages);
   
   $row = mysql_fetch_array($result);
   echo "<hope_message>";
   echo "<message> " . $row['message'] . "</message>";
   echo "<author>" . $row['author'] . "</author>";
   echo "</hope_message>";
    
   mysql_close($cid);
?>							