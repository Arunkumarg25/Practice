#!/usr/bin/perl 

#################################################################
#
# XML TC Gen
#
##################################################################
use strict;
use IO::Pty;
use XML::Writer;
use XML::Parser;
use File::Path;
use File::Copy;
use Text::CSV::Encoded;
use URI::Escape;

my @headers;
my @allvalues;

sub csv_query {
my ($file) = @_;

    my $csv = Text::CSV::Encoded->new({ encoding=>"utf8" });

    open (CSV, "<", $file) or die $!;

    my $count=0;
    while (<CSV>) {
      $count++;
        if ($csv->parse($_)) {
          if ($count == 1 ) {
            @headers = $csv->fields();
          } else {
            my @columns = $csv->fields();
            push @allvalues, \@columns;
          }
        } else {
            my $err = $csv->error_input;
            print "Failed to parse line: $err";
        }
    }
close CSV;
}

# generate xml file per TCID
sub gen_xml {
my ($wr, $line, $seqid, $location, $userid, $pw) = @_;
my $sid = $line+1;

    $wr->startTag( "action", acID=>$sid, stoptype=>"continue", timeout=>"");
    $wr->startTag( "actionName");
    $wr->characters("register");
    $wr->endTag("actionName");
    for (my $i=0; $i <= $#headers; $i++) {
      if ($allvalues[$line][$i] ne '' ) {
          $wr->emptyTag( "actionParam", paramType=>$headers[$i], paramValue=>$allvalues[$line][$i]);        
      }
    }
    $wr->endTag("action");
    $seqid++;

}

###############################################
#MAIN 
###############################################
if( ! defined $ARGV[1] ) {
        print "Usage: $0 <CSV FILE> <XML FILE> <WF ID> <PROFILE>\n";
        exit;
}

my ($csvfile, $xmlfile, $wfid, $profile) = @ARGV;
#mkpath($output_dir, 1, 0775);

# Pass and call mysql query subroutine 
 &csv_query($csvfile);

  my $doc = new IO::File(">$xmlfile");
  my $wr = new XML::Writer(OUTPUT => $doc, DATA_MODE => 'true', DATA_INDENT => 1, UNSAFE => 1);
  $wr->xmlDecl("UTF-8");
  $wr->startTag( "workflow", driver=>"selenium", wfID=>$wfid, profile=>$profile );

  for (my $i=0; $i <=$#allvalues ; $i++) {
    &gen_xml($wr, $i, 2) ;
  }

##################################
# end of xml file per tcid
##################################
  $wr->endTag( "workflow" );
  $wr->end;

  $doc->close();
  
  ###############################################
  # check xml file, initialize parser object and parse the string
  ###############################################
  my $parser = XML::Parser->new( ErrorContext => 2 );
  eval { $parser->parsefile( $xmlfile ); };
 
  # report any error that stopped parsing
  if( $@ ) {
      $@ =~ s/at \/.*?$//s;               # remove module line number
      print STDERR "\nERROR in '$xmlfile':\n$@\n";
  } else {
      print STDERR "'$xmlfile' is well-formed\n";
  }

##############################
# exit program
##############################
exit;
