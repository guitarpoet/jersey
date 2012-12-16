var Entity;

Entity = (function() {

  function Entity(table) {
    this.table = table;
  }

  Entity.prototype.fields = function() {
    var fields, p;
    fields = [];
    for (p in this) {
      if (typeof this[p] !== 'function') fields.push(p);
    }
    return fields;
  };

  Entity.prototype.save = function() {
    var args, end, field, first, sql, start, _i, _len, _ref;
    this.createDate = utils.now();
    this.modifyDate = utils.now();
    start = "insert into " + this.table + " (";
    first = true;
    end = ") values (";
    args = [];
    _ref = this.fields();
    for (_i = 0, _len = _ref.length; _i < _len; _i++) {
      field = _ref[_i];
      if (field === "table") continue;
      if (first) {
        first = false;
      } else {
        start += ",";
        end += ",";
      }
      start += field;
      end += "?";
      args.push(this[field]);
    }
    end += ")";
    sql = start + end;
    logger.info("The sql is '{}', and the args is {}", sql, sutils.toString(args));
    return db.up(sql, args);
  };

  Entity.prototype.remove = function() {
    var args, sql;
    sql = "delete from " + this.table + " where id = ?";
    args = [this.id];
    logger.info("The sql is '{}', and the args is {}", sql, sutils.toString(args));
    return db.update(sql, args);
  };

  Entity.prototype.update = function() {
    var args, field, sql, _i, _len, _ref;
    this.modifyDate = utils.now();
    sql = "update " + this.table + " ";
    args = [];
    _ref = this.fields();
    for (_i = 0, _len = _ref.length; _i < _len; _i++) {
      field = _ref[_i];
      if (field === "table" || field === "id") continue;
      sql += "set " + field + " = ? ";
      args.push(this[field]);
    }
    sql += "where id = ?";
    args.push(this.id);
    logger.info("The sql is '{}', and the args is {}", sql, sutils.toString(args));
    return db.up(sql, args);
  };

  return Entity;

})();
