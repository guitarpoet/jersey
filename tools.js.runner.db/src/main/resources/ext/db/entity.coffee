class Entity
	constructor: (@table)->

	fields: ->
		fields = []
		for p of this
			if typeof(this[p]) != 'function' # Skip all the functions of this object
				fields.push p
		return fields

	save: ->
		@createDate = now()
		@modifyDate = now()
		start = "insert into #{@table} ("
		first = true
		end = ") values ("
		args = []
		for field in this.fields()
			if field == "table"
				continue
			if first
				first = false
			else
				start += ","
				end += ","
			start += field
			end += "?"
			args.push this[field]
		end += ")"
		sql = start + end
		logger.info "The sql is '{}', and the args is {}", sql, sutils.toString(args)
		db.up sql, args

	remove: ->
		sql = "delete from #{@table} where id = ?"
		args = [@id]
		logger.info "The sql is '{}', and the args is {}", sql, sutils.toString(args)
		db.update sql, args

	update: ->
		@modifyDate = now()
		sql = "update #{@table} "
		args = []
		for field in this.fields()
			if field == "table" || field == "id"
				continue
			sql += "set #{field} = ? "
			args.push this[field]
		sql += "where id = ?"
		args.push @id
		logger.info "The sql is '{}', and the args is {}", sql, sutils.toString(args)
		db.up sql, args