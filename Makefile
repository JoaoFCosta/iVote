all: todos fixme

# Check if there are any 'TODO' left.
todos:
	grep -r -n "TODO" .

# Check if there are any 'FIXME' left.
fixme:
	grep -r -n "FIXME" .
